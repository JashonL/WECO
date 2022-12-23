package com.olp.weco.ui.dataloger

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.viewModels
import com.olp.weco.ui.common.fragment.RequestPermissionHub
import com.olp.weco.app.WECOApplication
import com.olp.weco.base.BaseActivity
import com.olp.weco.databinding.ActivitySetUpNetBinding
import com.olp.weco.monitor.WifiMonitor
import com.olp.weco.service.ble.BleCommand.BLUETOOTH_KEY
import com.olp.weco.service.ble.BleCommand.WIFI_PASSWORD
import com.olp.weco.service.ble.BleManager
import com.olp.weco.ui.dataloger.viewmodel.SetUpNetViewModel
import com.olp.weco.utils.ByteDataUtil
import com.olp.weco.utils.ByteDataUtil.BlueToothData.DATALOG_GETDATA_0X18
import com.olp.weco.view.dialog.AlertDialog
import com.olp.weco.view.pop.ListPopuwindow
import com.olp.weco.view.popuwindow.ListPopModel
import com.ttech.bluetooth.util.receiver.BlueToothReceiver
import com.ttech.bluetooth.util.util.LocalUtils
import com.olp.lib.util.LogUtil
import java.util.*


class SetUpNetActivity : BaseActivity(), OnClickListener {


    companion object {
        fun start(context: Context?) {
            context?.startActivity(Intent(context, SetUpNetActivity::class.java))
        }
    }


    private lateinit var binding: ActivitySetUpNetBinding


    private val viewModel: SetUpNetViewModel by viewModels()

    private var connectivityManager: ConnectivityManager? = null

    private var wifiManager: WifiManager? = null

    private var scanResults: List<ScanResult>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetUpNetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //android 12 通过监听  获取wifi名称
        wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {//android 12  31
            connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            requestNetwork()
        }

        //通知用户打开GPS
        initGps()

        initListeners()

        //监听扫描获取wifi列表
        WifiMonitor.watch(lifecycle) {
            it?.let {
                if (it.action == WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) {
                    scanResults = wifiManager!!.scanResults
                }
            }
        }

        initData()

    }

    private fun initData() {
        viewModel.responLiveData.observe(this) {
            //1.字节数组成bean
            if (it?.funcode == DATALOG_GETDATA_0X18) {
                val statusCode = it.statusCode
                val paramNum = it.paramNum
                if (paramNum == BLUETOOTH_KEY) { //连接成功
                    if (statusCode == 0) {
                        LogUtil.i(WECOApplication.APP_NAME, "发送密钥成功")
                    } else { //提示失败

                    }
                } else if (paramNum == WIFI_PASSWORD) {//设置WiFi名称密码回应
                    if (statusCode == 0) {
                        LogUtil.i(WECOApplication.APP_NAME, "设置wifi和密码设置成功......................")
                        DatalogerConActivity.start(this)
                    }
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        //扫描wifi
        wifiManager?.startScan()


        //接收蓝牙返回数据
        BlueToothReceiver.watch(lifecycle, WECOApplication.instance()) {
            val removePro = ByteDataUtil.BlueToothData.removePro(it)
            viewModel.parserData(it!![7], removePro)
        }



        viewModel.getBleManager(this, object : BleManager.bindServiceListeners {
            override fun onServiceConnected() {
                //发送密钥
                viewModel.sendCmdConnect()
            }

            override fun onServiceDisconnected() {}

        })

    }


    /**
     * 打开GPS获取当前连接的wifi名称  和获取wifi列表
     */
    private fun initGps() {
        val b: Boolean = LocalUtils.checkGPSIsOpen(this)
        if (b) {
            try {//获取当前连接wifi的名称 还有wifi列表
                initPermission()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {//去打开GPS
            //弹框提示
            showGpsDialog()

        }

    }


    /**
     * 开启GPS弹框
     */
    private fun showGpsDialog() {
        AlertDialog.showDialog(
            supportFragmentManager,
            getString(com.olp.weco.R.string.m104_gps_to_getwifi),
            getString(com.olp.weco.R.string.m101_cancel),
            getString(com.olp.weco.R.string.m102_comfir),
            getString(com.olp.weco.R.string.m103_turn_on_gps)
        ) {
            val intent = Intent()
            intent.action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
            startActivityForResult(intent, LocalUtils.OPEN_GPS_CODE)
        }
    }


    //检查权限 扫描wifi列表
    @SuppressLint("MissingPermission")
    private fun initPermission() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
        RequestPermissionHub.requestPermission(
            supportFragmentManager,
            permissions
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//android 8 26
                val mWifiManager =
                    (applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager)
                val info = mWifiManager.connectionInfo
                val ssid = info.ssid.replace("\"", "")
                binding.etSsid.setText(ssid)
            } else {
                val mWifiManager =
                    applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val connectionInfo = mWifiManager.connectionInfo
                val networkId = connectionInfo.networkId

                var ssid = connectionInfo.ssid
                val configuredNetworks = mWifiManager.configuredNetworks
                for (wificonf in configuredNetworks) {
                    if (wificonf.networkId == networkId) {
                        ssid = wificonf.SSID
                        break
                    }
                }
                if (TextUtils.isEmpty(ssid)) ssid = ""
                if (ssid.contains("\"")) {
                    ssid = ssid.replace("\"", "")
                }
                if (ssid.lowercase(Locale.getDefault()).contains("unknown ssid")) {
                    ssid = ""
                }

                binding.etSsid.setText(ssid)

            }
            scanResults = wifiManager?.scanResults
        }


    }

    private fun initListeners() {
        binding.ivWifiList.setOnClickListener(this)
        binding.btFinish.setOnClickListener(this)
    }


    override fun onClick(p0: View?) {
        when {
            p0 === binding.ivWifiList -> {
                showWifiList()
            }

            p0 === binding.btFinish -> {

                val ssid = binding.etSsid.text.toString()
                val pwd = binding.etPassword.text.toString()
                viewModel.requestSetting(ssid, pwd)

            }
        }

    }


    private fun showWifiList() {
        val options = mutableListOf<ListPopModel>()
        scanResults?.let {
            it.forEach {
                val ssid = it.SSID
                options.add(ListPopModel(ssid, false))
            }
        }
        val current = binding.etSsid.text.toString()
        ListPopuwindow.showPop(
            this,
            options,
            binding.llSsid,
            current
        ) {
            binding.etSsid.setText(options[it].title)
        }
    }


    private val request: NetworkRequest =
        NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build()


    private val mNetworkCallback =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            object : ConnectivityManager.NetworkCallback(FLAG_INCLUDE_LOCATION_INFO) {
                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val wifiInfo: WifiInfo = networkCapabilities.transportInfo as WifiInfo
                        val ssid =
                            wifiInfo.ssid.replace("\"", "").replace("<", "").replace(">", "")
                        binding.etSsid.setText(ssid)

                    }
                }
            }
        } else {
            null
        }


    private fun requestNetwork() {
        mNetworkCallback?.let { connectivityManager?.registerNetworkCallback(request, it) };
    }

    private fun unrequestNetwork() {
        mNetworkCallback?.let { connectivityManager?.unregisterNetworkCallback(it) };
    }


    override fun onDestroy() {
        super.onDestroy()
        unrequestNetwork()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LocalUtils.OPEN_GPS_CODE) {
            initGps()
        }
    }


}
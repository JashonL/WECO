package com.olp.weco.ui.dataloger

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.olp.weco.ui.common.activity.ScanActivity
import com.olp.weco.ui.common.fragment.RequestPermissionHub
import com.olp.weco.base.BaseActivity
import com.olp.weco.databinding.ActivityAddDataLoggerBinding
import com.olp.weco.service.ble.BleManager
import com.olp.weco.ui.station.viewmodel.AddDataLoggerViewModel
import com.olp.bluetooth.util.`interface`.IBleConnetLisener
import com.olp.bluetooth.util.`interface`.IScanResult
import com.olp.bluetooth.util.bean.BleModel
import com.olp.lib.util.ActivityBridge
import com.olp.lib.util.ToastUtil
import com.olp.lib.util.Util

/**
 * 添加采集器页面
 */
class AddDataLoggerActivity : BaseActivity(), View.OnClickListener {

    companion object {

        private const val KEY_PLANT_ID = "key_plant_id"
        private const val KEY_DATALOGER_SN = "key_dataloger_sn"

        fun start(context: Context?, plantId: String?, datalogSn: String?) {
            context?.startActivity(getIntent(context, plantId, datalogSn))
        }

        fun getIntent(context: Context?, plantId: String?, datalogSn: String?): Intent {
            return Intent(context, AddDataLoggerActivity::class.java).also {
                it.putExtra(KEY_PLANT_ID, plantId)
                it.putExtra(KEY_DATALOGER_SN, datalogSn)
            }
        }

    }

    private lateinit var binding: ActivityAddDataLoggerBinding
    private val viewModel: AddDataLoggerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDataLoggerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        setListener()
    }

    private fun initData() {
        viewModel.plantId = intent.getStringExtra(KEY_PLANT_ID)
        val datalogSn = intent.getStringExtra(KEY_DATALOGER_SN)
        val validate = Util.validateWebbox(datalogSn)
        binding.etDataLoggerSn.setText(datalogSn)
        binding.etCheckCode.setText(validate)

        viewModel.addDataLoggerLiveData.observe(this) {
            dismissDialog()
            if (it == null) {
                //开始查找并且连接对应蓝牙
                connectBle()
            } else {
                ToastUtil.show(it)
            }
        }
        viewModel.getCheckCodeLiveData.observe(this) {
            dismissDialog()
            if (it.second == null) {
                binding.etCheckCode.setText(it.first)
            } else {
                ToastUtil.show(it.second)
            }
        }
    }

    private fun setListener() {
        binding.ivScan.setOnClickListener(this)
        binding.btFinish.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when {
            v === binding.ivScan -> {
                RequestPermissionHub.requestPermission(
                    supportFragmentManager,
                    arrayOf(Manifest.permission.CAMERA)
                ) {
                    if (it) {
                        scan()
                    }
                }
            }
            v === binding.btFinish -> {
                connectBle()


      /*          val dataLoggerSN = binding.etDataLoggerSn.text.toString().trim()
                val checkCode = binding.etCheckCode.text.toString().trim()
                when {
                    dataLoggerSN.isEmpty() -> {
                        ToastUtil.show(getString(R.string.m90_serial_number_not_null))
                    }
                    checkCode.isEmpty() -> {
                        ToastUtil.show(getString(R.string.m91_check_code_not_null))
                    }
                    else -> {
                        showDialog()
                        viewModel.addDataLogger(dataLoggerSN, checkCode)
                    }
                }*/
            }
        }
    }

    private fun scan() {
        ActivityBridge.startActivity(
            this,
            ScanActivity.getIntent(this,viewModel.plantId.toString()),
            object : ActivityBridge.OnActivityForResult {
                override fun onActivityForResult(
                    context: Context?,
                    resultCode: Int,
                    data: Intent?
                ) {
                    if (resultCode == RESULT_OK && data?.hasExtra(ScanActivity.KEY_CODE_TEXT) == true) {
                        val dataLoggerSN = data.getStringExtra(ScanActivity.KEY_CODE_TEXT)
                        binding.etDataLoggerSn.setText(dataLoggerSN)
                        dataLoggerSN?.also {
                            showDialog()
                            binding.etCheckCode.setText(Util.validateWebbox(it))
                        }
                    }
                }
            })
    }


    private fun connectBle() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_ADVERTISE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }

        RequestPermissionHub.requestPermission(
            supportFragmentManager,
            permissions
        ) {
            if (it) {
                viewModel.getBleManager(this, object : BleManager.bindServiceListeners {
                    override fun onServiceConnected() {
                        //判断是否已经打开蓝牙
                        val bleEnable = viewModel.bleManager?.isBleEnable()
                        if (bleEnable == true) {
                            //搜索对应的蓝牙设备 并且连接
                            viewModel.bleManager?.scan(object : IScanResult {
                                override fun scanning(model: BleModel) {
                                    val name = model.name
                                    binding.etDataLoggerSn.text?.let {
                                        if (it.toString() == name) {
                                            viewModel.bleManager?.stopScan()
                                        }


                                    }
                                }

                                override fun scanResult(results: List<BleModel>) {
                                    val text = binding.etDataLoggerSn.text.toString()
                                    //找到对应的蓝牙-连接
                                    results.forEach {
                                        if (text==it.name){
                                            val address = it.address

                                            //去连接蓝牙
                                            viewModel.bleManager?.connect(address?:"",object :
                                                IBleConnetLisener {
                                                override fun connectError() {
                                                    //连接失败了
                                                }

                                                override fun connectSuccess() {
                                                    //连接成功跳转下一步
                                                    SetUpNetActivity.start(this@AddDataLoggerActivity)
                                                }

                                                override fun responData(receviceData: ByteArray?) {

                                                }

                                            })
                                            return
                                        }

                                    }
                                }

                            })

                        } else {
                            //打开蓝牙
                            viewModel.bleManager?.openBle()
                        }
                    }

                    override fun onServiceDisconnected() {

                    }

                })
            }
        }
    }


}
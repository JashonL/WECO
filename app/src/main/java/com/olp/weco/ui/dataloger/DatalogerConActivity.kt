package com.olp.weco.ui.dataloger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.olp.bluetooth.util.receiver.BlueToothReceiver
import com.olp.lib.util.LogUtil
import com.olp.lib.util.gone
import com.olp.lib.util.visible
import com.olp.weco.app.WECOApplication
import com.olp.weco.base.BaseActivity
import com.olp.weco.databinding.ActivityDatalogerConnectingBinding
import com.olp.weco.model.ble.DatalogResponBean
import com.olp.weco.service.ble.BleCommand.CHECKCONNET_STATUS
import com.olp.weco.service.ble.BleCommand.LINK_STATUS
import com.olp.weco.service.ble.BleManager
import com.olp.weco.ui.MainActivity
import com.olp.weco.ui.dataloger.viewmodel.ConnetViewModel
import com.olp.weco.utils.ByteDataUtil
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch


class DatalogerConActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, DatalogerConActivity::class.java))

        }

    }


    private lateinit var binding: ActivityDatalogerConnectingBinding


    private val viewModel: ConnetViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDatalogerConnectingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initLiseners()
        initData()

    }

    private fun initLiseners() {
        binding.configed.ivOk.setOnClickListener {
            MainActivity.start(this)
            finish()
        }


        binding.configFail.configFail.setOnClickListener {
            //开始计时
            startTimer()
            //获取配网状态
            viewModel.checkStatus()
        }

        binding.configFail.btRewiring.setOnClickListener {
            finish()
        }


    }

    private fun initView() {

        binding.configed.configed.gone()
        binding.configing.configing.visible()
        binding.configFail.configFail.gone()
    }


    override fun onResume() {
        super.onResume()
        //接收蓝牙返回数据
        BlueToothReceiver.watch(lifecycle, WECOApplication.instance()) {
            val removePro = ByteDataUtil.BlueToothData.removePro(it)
            viewModel.parserData(it!![7], removePro)
        }



        viewModel.getBleManager(this, object : BleManager.bindServiceListeners {
            override fun onServiceConnected() {
                //开始计时
                startTimer()
                //获取配网状态
                viewModel.checkStatus()
            }

            override fun onServiceDisconnected() {}

        })
    }


    private fun initData() {
        viewModel.responLiveData.observe(this) {
            //1.字节数组成bean
            if (it?.funcode == ByteDataUtil.BlueToothData.DATALOG_GETDATA_0X19) {
                val paramBeanList: List<DatalogResponBean.ParamBean> = it.paramBeanList
                for (i in paramBeanList.indices) {
                    val paramBean = paramBeanList[i]
                    val num = paramBean.num
                    val value = paramBean.value

                    //查询配网状态  一直到连接服务器才会返回60，如果要判断是否采集器是否连接上路由器请求55
                    if (num == CHECKCONNET_STATUS) {
                        LogUtil.i(WECOApplication.APP_NAME, "连接状态......................$value")
                        //连接错误
                        if ("0" == value) { //连接成功
                            viewModel.checkServerStatus()
                        } else if ("255".equals(value, ignoreCase = true)) {
                            viewModel.checkStatus() //查询配网状态
                        }
                    }
                    if (num == LINK_STATUS) {
                        LogUtil.i(WECOApplication.APP_NAME, "连接状态......................$value")
                        //连接错误
                        val status = value.toInt()
                        if (status == 4 || status == 3 || status == 16) { //连接成功
                            LogUtil.i(WECOApplication.APP_NAME, "配网成功......................$value")
                            configSuccess()
                        } else {
                            viewModel.checkServerStatus() //查询配网状态

                        }
                    }
                }
            }
        }
    }


    private fun configSuccess() {
        binding.configed.configed.visible()
        binding.configing.configing.gone()
        binding.configFail.configFail.gone()
        stopTimer()

    }


    private fun configFail() {
        binding.configed.configed.gone()
        binding.configing.configing.gone()
        binding.configFail.configFail.visible()
        stopTimer()
    }

    private var job: Job? = null

    /**
     * 计时120秒
     */
    private fun startTimer() {
        var count = 120
        job = lifecycleScope.launch {
            while (count > 0) {
                ensureActive()
                count--
                delay(1000)
            }

            //显示失败界面 并且停止协程
            configFail()
        }
    }


    /**
     * 计时120秒
     */
    private fun stopTimer() {
        job?.cancel()
    }

}






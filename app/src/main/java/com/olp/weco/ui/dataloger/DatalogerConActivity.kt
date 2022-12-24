package com.olp.weco.ui.dataloger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.olp.weco.app.WECOApplication
import com.olp.weco.base.BaseActivity
import com.olp.weco.databinding.ActivityDatalogerConnectingBinding
import com.olp.weco.model.ble.DatalogResponBean.ParamBean
import com.olp.weco.service.ble.BleCommand.CHECKCONNET_STATUS
import com.olp.weco.service.ble.BleCommand.LINK_STATUS
import com.olp.weco.service.ble.BleManager
import com.olp.weco.ui.dataloger.viewmodel.ConnetViewModel
import com.olp.weco.utils.ByteDataUtil
import com.olp.bluetooth.util.receiver.BlueToothReceiver
import com.olp.lib.util.LogUtil


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


        initData()

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
                val paramBeanList: List<ParamBean> = it.paramBeanList
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
                        } else {
                            viewModel.checkServerStatus() //查询配网状态

                        }
                    }
                }
            }
        }
    }


}






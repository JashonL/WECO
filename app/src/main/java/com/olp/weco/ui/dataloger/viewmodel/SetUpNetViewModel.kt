package com.olp.weco.ui.dataloger.viewmodel


import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.olp.weco.base.BaseViewModel
import com.olp.weco.model.ble.DatalogAPSetParam
import com.olp.weco.model.ble.DatalogResponBean
import com.olp.weco.service.ble.BleCommand.BLUETOOTH_KEY
import com.olp.weco.service.ble.BleCommand.WIFI_PASSWORD
import com.olp.weco.service.ble.BleCommand.WIFI_SSID
import com.olp.weco.service.ble.BleManager
import com.olp.weco.utils.ByteDataUtil
import com.olp.weco.utils.ByteDataUtil.BlueToothData.DATALOG_GETDATA_0X18


class SetUpNetViewModel : BaseViewModel() {

    private val bleLiveData = MutableLiveData<BleManager?>()

    var bleManager: BleManager? = null

    var devId: String = "0000000000"

     val responLiveData = MutableLiveData<DatalogResponBean?>()


    fun getBleManager(context: Context, bindServiceListeners: BleManager.bindServiceListeners) {
        bleLiveData.value = BleManager(context, bindServiceListeners)
        bleManager = bleLiveData.value
    }


    /*发送密钥连接蓝牙*/
    fun sendCmdConnect() {
        //蓝牙公共密钥
        val bluetoothCommentKey = BleManager.BLUETOOTH_OSS_KEY

        val restartList: MutableList<DatalogAPSetParam> = ArrayList()
        val bean = DatalogAPSetParam()
        bean.paramnum = BLUETOOTH_KEY
        bean.length = bluetoothCommentKey.length
        bean.value = bluetoothCommentKey
        restartList.add(bean)
        val numberServerPro18 =
            ByteDataUtil.BlueToothData.numberServerPro18(DATALOG_GETDATA_0X18, devId, restartList)
        bleManager?.sendData(numberServerPro18)
    }


    /**
     * 设置wifi账号密码
     */
    fun requestSetting(ssid: String, password: String) {
        //请求重置采集器
        val routerList: MutableList<DatalogAPSetParam> = ArrayList()
        val bean = DatalogAPSetParam()
        bean.apply {
            this.paramnum = WIFI_SSID
            this.length = ssid.length
            this.value = ssid
        }
        routerList.add(bean)

        val bean1 = DatalogAPSetParam()
        bean1.apply {
            this.paramnum = WIFI_PASSWORD
            this.length = password.length
            this.value = password
        }
        routerList.add(bean1)
        val numberServerPro18 =
            ByteDataUtil.BlueToothData.numberServerPro18(DATALOG_GETDATA_0X18, devId, routerList)
        bleManager?.sendData(numberServerPro18)

    }


    /**
     * 解析返回数据
     */
    fun parserData(byte: Byte,dataByte: ByteArray?) {
        dataByte?.let {
            responLiveData.value = ByteDataUtil.BlueToothData.paserData(byte,it)

        }
    }


}
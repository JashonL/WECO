package com.olp.weco.service.ble

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.olp.bluetooth.util.`interface`.IBleConnect
import com.olp.bluetooth.util.`interface`.IBleConnetLisener
import com.olp.bluetooth.util.`interface`.IScanResult
import com.olp.bluetooth.util.service.BleConnectService

class BleManager(context: Context, bindServiceListeners: bindServiceListeners) : IBleConnect {

    companion object{
        //oss账户使用密钥
        const val BLUETOOTH_OSS_KEY="growatt_iot_device_common_key_01"
    }


    //保持所启动的Service的IBinder对象,同时定义一个ServiceConnection对象
    var binder: BleConnectService.LocalBinder? = null
    private val conn: ServiceConnection = object : ServiceConnection {
        //Activity与Service断开连接时回调该方法
        override fun onServiceDisconnected(name: ComponentName) {
        }

        //Activity与Service连接成功时回调该方法
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            binder = service as BleConnectService.LocalBinder
            bindServiceListeners.onServiceConnected()

        }
    }

    init {
        val intent = Intent(context, BleConnectService::class.java)
        context.bindService(intent, conn, AppCompatActivity.BIND_AUTO_CREATE)
        context.startService(intent)
    }


    override fun isBleEnable(): Boolean? {
        return binder?.service?.isBleEnable()
    }


    override fun openBle() {
        binder?.service?.openBle()
    }

    override fun scan(callback: IScanResult?) {
        binder?.service?.scan(callback)
    }



    override fun stopScan() {
        binder?.service?.stopScan()
    }

    override fun connect(mac: String, callback: IBleConnetLisener?) {
        binder?.service?.connect(mac,callback)
    }



    override fun setMtu(mtu: Int,callback: IBleConnetLisener?) {
        binder?.service?.setMtu(mtu,callback)
    }

    override fun sendData(value: ByteArray) {
        binder?.service?.sendData(value)
    }

    override fun receiveData(data: ByteArray?,callback: IBleConnetLisener?) {
        binder?.service?.receiveData(data,callback)
    }

    override fun disConnect() {
        binder?.service?.disConnect()
    }



    interface bindServiceListeners {
        fun onServiceConnected()
        fun onServiceDisconnected()
    }

}
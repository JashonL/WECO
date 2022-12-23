package com.ttech.bluetooth.util.receiver

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class BlueToothReceiver(val app:Application) : BroadcastReceiver(), LifecycleObserver {


    private var listener: ((data: ByteArray?) -> Unit)? = null

    companion object {
         const val BLUETOOTH_RECEIVER_DATA = "blueToothReceiver"
         const val BLUETOOTH_RECEIVER_VALUE = "blue_tooth_value"

        /**
         * 使用高阶函数，将函数作为参数传进去，减少创建接口
         */
        fun watch(
            lifecycle: Lifecycle? = null,
            app:Application,
            listener: (data: ByteArray?) -> Unit
        ): BlueToothReceiver {
            val intentFilter = IntentFilter()
            intentFilter.addAction(BLUETOOTH_RECEIVER_DATA)
            val monitor = BlueToothReceiver(app)
            monitor.listener = listener
            lifecycle?.addObserver(monitor)
            LocalBroadcastManager.getInstance(app)
                .registerReceiver(monitor, intentFilter)
            return monitor
        }



        fun notify(context: Context?,intent: Intent) {
            context?.let {
                LocalBroadcastManager.getInstance(it)
                    .sendBroadcast(intent)
            }
        }


    }



    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (it.action == BLUETOOTH_RECEIVER_DATA) {
                val dataByte = it.getByteArrayExtra(BLUETOOTH_RECEIVER_VALUE)
                listener?.invoke(dataByte)
            }
        }
    }




    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun unWatch() {
        listener = null
        LocalBroadcastManager.getInstance(app).unregisterReceiver(this)
    }



}
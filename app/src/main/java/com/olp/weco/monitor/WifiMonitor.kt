package com.olp.weco.monitor

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.olp.weco.app.WECOApplication

class WifiMonitor :BroadcastReceiver(),LifecycleObserver{

    private var listener: ((intent: Intent?) -> Unit)? = null

    companion object {

        /**
         * 使用高阶函数，将函数作为参数传进去，减少创建接口
         */
        fun watch(
            lifecycle: Lifecycle? = null,
            listener: (intent: Intent?) -> Unit
        ): WifiMonitor {
            val intentFilter = IntentFilter()
            intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
            val monitor = WifiMonitor()
            monitor.listener = listener
            lifecycle?.addObserver(monitor)
            LocalBroadcastManager.getInstance(WECOApplication.instance())
                .registerReceiver(monitor, intentFilter)
            return monitor
        }


    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (it.action == WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) {
                listener?.invoke(intent)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun unWatch() {
        listener = null
        LocalBroadcastManager.getInstance(WECOApplication.instance()).unregisterReceiver(this)
    }
}
package com.olp.weco.ui.station.monitor

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.olp.weco.app.WECOApplication

/**
 * 电站修改监听
 * 1.新增电站
 * 2.编辑电站
 * 3.删除电站
 */
class PlantMonitor : BroadcastReceiver(), LifecycleObserver {

    private var listener: ((monitor: PlantMonitor) -> Unit)? = null

    companion object {

        private const val ACTION_PLANT = "action_plant"

        /**
         * 使用高阶函数，将函数作为参数传进去，减少创建接口
         */
        fun watch(
            lifecycle: Lifecycle? = null,
            listener: (monitor: PlantMonitor) -> Unit
        ): PlantMonitor {
            val intentFilter = IntentFilter()
            intentFilter.addAction(ACTION_PLANT)
            val monitor = PlantMonitor()
            monitor.listener = listener
            lifecycle?.addObserver(monitor)
            LocalBroadcastManager.getInstance(WECOApplication.instance())
                .registerReceiver(monitor, intentFilter)
            return monitor
        }

        fun notifyPlant() {
            LocalBroadcastManager.getInstance(WECOApplication.instance())
                .sendBroadcast(Intent().apply {
                    action = ACTION_PLANT
                })
        }

    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (it.action == ACTION_PLANT) {
                listener?.invoke(this)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun unWatch() {
        listener = null
        LocalBroadcastManager.getInstance(WECOApplication.instance()).unregisterReceiver(this)
    }
}
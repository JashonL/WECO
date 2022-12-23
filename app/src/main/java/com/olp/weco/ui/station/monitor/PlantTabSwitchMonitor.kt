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
 * 电站列表tab切换
 */
class PlantTabSwitchMonitor : BroadcastReceiver(), LifecycleObserver {

    private var listener: ((monitor: PlantTabSwitchMonitor, position: Int) -> Unit)? = null

    companion object {

        private const val ACTION_PLANT_TAB = "action_plant_tab"
        private const val KEY_POSITION = "key_position"

        /**
         * 使用高阶函数，将函数作为参数传进去，减少创建接口
         */
        fun watch(
            lifecycle: Lifecycle? = null,
            listener: (monitor: PlantTabSwitchMonitor, position: Int) -> Unit
        ): PlantTabSwitchMonitor {
            val intentFilter = IntentFilter()
            intentFilter.addAction(ACTION_PLANT_TAB)
            val monitor = PlantTabSwitchMonitor()
            monitor.listener = listener
            lifecycle?.addObserver(monitor)
            LocalBroadcastManager.getInstance(WECOApplication.instance())
                .registerReceiver(monitor, intentFilter)
            return monitor
        }

        fun notifyPlantSwitch(position: Int) {
            LocalBroadcastManager.getInstance(WECOApplication.instance())
                .sendBroadcast(Intent().apply {
                    action = ACTION_PLANT_TAB
                    putExtra(KEY_POSITION, position)
                })
        }

    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (it.action == ACTION_PLANT_TAB) {
                listener?.invoke(this, it.getIntExtra(KEY_POSITION, 0))
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun unWatch() {
        listener = null
        LocalBroadcastManager.getInstance(WECOApplication.instance()).unregisterReceiver(this)
    }
}
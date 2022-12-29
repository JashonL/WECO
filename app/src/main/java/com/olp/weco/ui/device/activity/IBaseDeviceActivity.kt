package com.olp.weco.ui.device.activity

import android.content.Context
import com.olp.weco.model.DeviceType

/**
 * 设备详情基类
 */
interface IBaseDeviceActivity {

    companion object {
        /**
         * 跳转到设备详情页面
         */
        fun jumpToDeviceInfoPage(
            context: Context?,
            @DeviceType deviceType: Int,
            deviceSN: String?
        ) {
            when (deviceType) {
                DeviceType.PCS -> {}
                DeviceType.XP -> {}
                DeviceType.HVBOX -> {}
            }
        }
    }

    fun getDeviceType(): Int
}
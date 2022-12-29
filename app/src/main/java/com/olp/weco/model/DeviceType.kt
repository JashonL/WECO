package com.olp.weco.model

import androidx.annotation.IntDef
import com.olp.weco.R
import com.olp.weco.app.WECOApplication

/**
 * 设备状态
 */
@IntDef(
    DeviceType.PCS,
    DeviceType.XP,
    DeviceType.HVBOX,
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class DeviceType {
    companion object {
        const val PCS = 1
        const val XP = 2
        const val HVBOX = 3

        fun getDeviceTypeName(@DeviceType deviceType: Int): String {
            return WECOApplication.instance().getString(
                when (deviceType) {
                    PCS -> R.string.m157_hps
                    XP -> R.string.m158_xp
                    else -> R.string.m159_hvbox
                }
            )
        }
    }
}
package com.olp.weco.model


/**
 * 设备列表Model
 */
data class DeviceModel(
    val deviceName: String?,
    val deviceSn: String?,
    val deviceTypeText: String?,
    val deviceType: Int?,

) {

    fun getRealDeviceType(): Int {
        return deviceType ?: 0
    }


}

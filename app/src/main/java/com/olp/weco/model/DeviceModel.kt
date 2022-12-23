package com.olp.weco.model


/**
 * 设备列表Model
 */
data class DeviceModel(
    val deviceType: String?,//设备类型
    val deviceModel: String?,//设备型号
    val deviceSn: String?,//设备SN
    val soc: Int?,//剩余电量
    val sysStatus: Int?,// 系统状态 -1充电 0 无数据 +1 放电
    val eToday: Double?,//今日发电量
    val eTotal: Double?,//累计发电量
    val lost: Boolean?,//1.(true-离线、false-在线),2.(true-未连接、false-已连接)
    val interval: String?,//采集器-更新间隔，"0"
    val lastUpdate: String?,//采集器-最后更新时间，"2020-06-01 07:00:48"
    val power: Double?,//Combiner汇流箱-功率1.0
    val vol: Double?,//Combiner汇流箱-电压1.0
    val cur: Double?,//Combiner汇流箱-电流2.0
    val signal: String?,//采集器-良
) {





}

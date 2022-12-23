package com.olp.weco.model

/**
 * 电站状态数量统计
 */
data class PlantStatusNumModel(
    val allNum: Int = 0,
    val onlineNum: Int = 0,
    val offline: Int = 0,
    val faultNum: Int = 0
)

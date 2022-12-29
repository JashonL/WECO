package com.olp.weco.ui.home.model

data class StorageModel(
    val pvPower: String,
    val elcGridPower: String?,
    val loadPower: String?,
    val batteryPower: String?,
    val pvDayChargeTotal: String?,
    val selfElc: String?,
    val batterySoc: String?,
    val plantStatus: String?,
    val unReadCount: String?,
    val deviceCount:String
)


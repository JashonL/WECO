package com.olp.weco.ui.message.model

data class HmiDataFaultMessage(
    val deviceName: Any,
    val faultCode: Int,
    val faultDescription: String,
    val faultId: Int,
    val faultTime: String,
    val plantName: String,
    val readStatus: Int,
    val solution: String
)
package com.olp.weco.ui.device.model

data class DeviceSocDataModel(
    val title:String,
    val soc:String,
    val percent:Double,
    val MaxVoltage:String,
    val MinVoltage:String,
    val Maxtemperature:String,
    val Mintemperature:String,
)
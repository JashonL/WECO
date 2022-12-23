package com.olp.weco.model.energy

data class ChartModel(
    val solarTotal: String,
    val gridTotal: String,
    val batTotal: String,
    val loadTotal: String,
    val energyTotal: String,
    val solarList: Array<Float>,
    val gridList: Array<Float>,
    val batList: Array<Float>,
    val loadList: Array<Float>
)


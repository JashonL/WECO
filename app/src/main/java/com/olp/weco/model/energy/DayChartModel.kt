package com.olp.weco.model.energy

data class DayChartModel (
    val lowPower: String,
    val avgPower: String,
    val highPower: String,
    val energy: String,
    val power: Array<Float>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DayChartModel

        if (lowPower != other.lowPower) return false
        if (avgPower != other.avgPower) return false
        if (highPower != other.highPower) return false
        if (energy != other.energy) return false
        if (!power.contentEquals(other.power)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = lowPower.hashCode()
        result = 31 * result + avgPower.hashCode()
        result = 31 * result + highPower.hashCode()
        result = 31 * result + energy.hashCode()
        result = 31 * result + power.contentHashCode()
        return result
    }
}
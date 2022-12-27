package com.olp.weco.model.energy

data class ChartModel(
    val lowEnergy: String,
    val avgEnergy: String,
    val highEnergy: String,
    val totalEnergy: String,
    val energy: Array<Float>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChartModel

        if (lowEnergy != other.lowEnergy) return false
        if (avgEnergy != other.avgEnergy) return false
        if (highEnergy != other.highEnergy) return false
        if (totalEnergy != other.totalEnergy) return false
        if (!energy.contentEquals(other.energy)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = lowEnergy.hashCode()
        result = 31 * result + avgEnergy.hashCode()
        result = 31 * result + highEnergy.hashCode()
        result = 31 * result + totalEnergy.hashCode()
        result = 31 * result + energy.contentHashCode()
        return result
    }
}






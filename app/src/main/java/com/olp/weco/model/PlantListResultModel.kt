package com.olp.weco.model

/**
 * 电站列表后台返回的数据模型
 */
data class PlantListResultModel(
    val plantList: Array<PlantModel>,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlantListResultModel

        if (!plantList.contentEquals(other.plantList)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = plantList.contentHashCode()
        return result
    }

}

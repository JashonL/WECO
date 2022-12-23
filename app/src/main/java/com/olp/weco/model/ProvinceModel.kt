package com.olp.weco.model

import com.olp.weco.view.dialog.ItemName


/**
 * 省份
 */
data class ProvinceModel(val name: String, val citys: Array<CityModel>?) : ItemName {

    override fun itemName(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProvinceModel

        if (name != other.name) return false
        if (!citys.contentEquals(other.citys)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + citys.contentHashCode()
        return result
    }
}

/**
 * 城市
 */
data class CityModel(val city: String) : ItemName {

    override fun itemName(): String {
        return city
    }

}

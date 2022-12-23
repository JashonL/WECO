package com.olp.weco.model

/**
 * 时区列表model，包含默认货币单位
 */
data class TimeZone(val monetaryUnit: String, val timezoneList: Array<String>) {

    /**
     * 封装为Picker选择器Model
     */
    fun getGeneralItem(): Array<GeneralItemModel> {
        if (timezoneList.isNullOrEmpty()) {
            return emptyArray()
        }
        return Array(timezoneList.size) { index -> GeneralItemModel(timezoneList[index]) }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TimeZone

        if (monetaryUnit != other.monetaryUnit) return false
        if (!timezoneList.contentEquals(other.timezoneList)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = monetaryUnit.hashCode()
        result = 31 * result + timezoneList.contentHashCode()
        return result
    }
}
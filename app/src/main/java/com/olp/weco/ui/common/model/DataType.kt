package com.olp.weco.ui.common.model

import androidx.annotation.IntDef
import com.olp.weco.R
import com.olp.weco.app.WECOApplication

/**
 * 搜索Type
 */
@IntDef(
    DataType.TOTAL,
    DataType.YEAR,
    DataType.MONTH,
    DataType.DAY
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD)
annotation class DataType {
    companion object {
        const val TOTAL = 0
        const val YEAR = 1
        const val MONTH = 2
        const val DAY = 3


        fun getDateNameByType(@DataType type: Int): String {
            return when (type) {
                TOTAL -> WECOApplication.instance().getString(R.string.m89_total)
                YEAR ->  WECOApplication.instance().getString(R.string.m72_year)
                MONTH ->  WECOApplication.instance().getString(R.string.m71_month)
                DAY ->  WECOApplication.instance().getString(R.string.m70_day)
                else -> {
                    ""
                }
            }
        }

    }


}
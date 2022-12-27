package com.olp.weco.ui.energy

import androidx.annotation.IntDef
import com.olp.weco.R
import com.olp.weco.app.WECOApplication
import com.olp.weco.ui.common.model.DataType


/**
 * 搜索Type
 */
@IntDef(
    ChartType.HOME,
    ChartType.PPV,
    ChartType.GRID,
    ChartType.BAT
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD)
annotation class ChartType {
    companion object {
        const val HOME = 1
        const val PPV = 2
        const val GRID = 3
        const val BAT = 4

        fun getNameByType(@ChartType type: Int): String {
            return when (type) {
                HOME -> WECOApplication.instance().getString(R.string.m16_home)
                PPV -> WECOApplication.instance().getString(R.string.m9_solar)
                GRID -> WECOApplication.instance().getString(R.string.m12_grid)
                BAT -> WECOApplication.instance().getString(R.string.m33_bat)
                else -> {
                    ""
                }
            }
        }

    }

}
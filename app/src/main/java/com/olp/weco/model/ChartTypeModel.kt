package com.olp.weco.model

import android.graphics.Color
import android.os.Parcelable
import androidx.annotation.ColorInt
import kotlinx.android.parcel.Parcelize

/**
 * 图表类型
 */
data class ChartTypeModel(
    val type: String,//图表类型，对应上传给服务端字段，大类型
    val typeName: String,//大类型名称
    val typeUnit: String//类型单位
) {
    companion object {
        fun getTypeNames(chartTypes: Array<ChartTypeModel>): Array<String> {
            return Array(chartTypes.size) { index -> chartTypes[index].typeName }
        }

        fun createChartColors(): List<ChartColor> {
            val chartColors = mutableListOf<ChartColor>()
            chartColors.add(
                ChartColor(
                    Color.parseColor("#FF3FAE29"),
                    Color.parseColor("#333FAE29")
                )
            )
            chartColors.add(
                ChartColor(
                    Color.parseColor("#FF67ADFF"),
                    Color.parseColor("#3367ADFF")
                )
            )
            chartColors.add(
                ChartColor(
                    Color.parseColor("#FFFF6434"),
                    Color.parseColor("#33FF6434")
                )
            )
            chartColors.add(
                ChartColor(
                    Color.parseColor("#FFFEB64D"),
                    Color.parseColor("#33FEB64D")
                )
            )
            chartColors.add(
                ChartColor(
                    Color.parseColor("#FF82DCDC"),
                    Color.parseColor("#3382DCDC")
                )
            )
            chartColors.add(
                ChartColor(
                    Color.parseColor("#FFD4EC59"),
                    Color.parseColor("#33D4EC59")
                )
            )
            chartColors.add(
                ChartColor(
                    Color.parseColor("#FFD3B5F1"),
                    Color.parseColor("#33D3B5F1")
                )
            )
            return chartColors
        }
    }
}

@Parcelize
data class ChartColor(@ColorInt val color: Int, @ColorInt val alphaColor: Int) : Parcelable
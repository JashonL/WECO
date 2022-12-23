package com.olp.weco.ui.chart

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.olp.weco.R
import com.olp.weco.base.BaseFragment
import com.olp.weco.databinding.FragmentLineChartBinding
import com.olp.weco.model.ChartColor
import com.olp.weco.ui.energy.chart.EnergyChartFragment.Companion.COLORS
import com.olp.weco.ui.energy.chart.EnergyChartFragment.Companion.DATALIST_KEY
import com.olp.weco.ui.energy.chart.EnergyChartFragment.Companion.UNIT
import com.olp.weco.view.ChartLegend
import com.olp.lib.util.GsonManager
import com.olp.lib.util.LogUtil
import com.olp.lib.util.Util

class LineChartFragment : BaseFragment() {

    private lateinit var _binding: FragmentLineChartBinding


    private var colors = arrayListOf<ChartColor>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        LogUtil.i("liaojinsha", "LineChartFragment onCreateView")
        _binding = FragmentLineChartBinding.inflate(inflater, container, false)

        arguments.apply {
            val datalist = this?.getString(DATALIST_KEY)
            val unit = this?.getString(UNIT)
            val chartListDataModel =
                datalist?.let { GsonManager.fromJson(it, ChartListDataModel::class.java) }

            colors=this?.getParcelableArrayList(COLORS)?: arrayListOf(
                ChartColor(Color.parseColor("#F6F6F8"), Color.parseColor("#33F6F6F8")),
                ChartColor(Color.parseColor("#999999"), Color.parseColor("#33999999")),
                ChartColor(Color.parseColor("#80DA8A"), Color.parseColor("#3380DA8A")),
                ChartColor(Color.parseColor("#5E72E4"), Color.parseColor("#335E72E4"))
            )

            refresh(chartListDataModel, unit)
        }

        return _binding.root
    }


    fun refresh(chartListDataModel: ChartListDataModel?, unit: String?) {
        LogUtil.i("liaojinsha", "LineChartFragment刷新数据")
        _binding.tvUnit.text = unit
        chartListDataModel?.let {
            showChartData(it)
        }

    }


    fun showChartData(data: ChartListDataModel) {
        initChartView(data)
        setchartData(data)
    }


    private fun initChartView(data: ChartListDataModel) {
        val timeList = data.timeList
        val multipleChartMarkView = MultipleChartMarkView(requireContext())
        _binding.lineChart.also {
            it.setDrawGridBackground(false)
            it.description.isEnabled = false //不显示描述（右下角）
            it.setTouchEnabled(true)//设置是否能触摸
            it.isScaleXEnabled = true//设置X轴能缩小放大,配合setTouchEnabled使用
            it.isScaleYEnabled = false//设置Y轴能缩小放大,配合setTouchEnabled使用
            it.isDragEnabled = true//设置能够拖动
            it.axisRight.isEnabled = false//设置右边Y轴线不显示
            it.legend.isEnabled = false//是否显示类型图标
/*
            it.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    val dataSets = it.data.dataSets
                    val chartDataValues: MutableList<ChartDataValue> = mutableListOf()
                    val x = e?.x ?: 0f
                    for (index in dataSets.indices) {
                        val barDataSet = dataSets[index]
                        //取整再加0.5，获取正确的值
                        val y = barDataSet.getEntryForXValue(
                            x.toInt().toFloat() + 0.5f,
                            barDataSet.yMax
                        ).y
                        chartDataValues.add(
                            ChartDataValue(
                                barDataSet.color,
                                barDataSet.label,
                                Util.getDoubleText(y.toDouble())
                            )
                        )
                        if (index == 0) {
                            it.highlightValue(Highlight(x, y, index))
                        }
                    }
                    marker.data = ChartMarkerViewData(
                        chartListDataModel?.timeList?.get(x.toInt()) ?: "",
                        chartDataValues.toTypedArray()
                    )
                }

                override fun onNothingSelected() {

                }

            })

            marker.chartView = _binding.barChart*/



            _binding.lineChart.marker = multipleChartMarkView
            multipleChartMarkView.chartView = it
        }

        //X轴
        _binding.lineChart.xAxis.also {
            it.isEnabled = true //设置X轴启用
            it.position = XAxis.XAxisPosition.BOTTOM //设置X轴坐标值显示的位置
            it.setDrawGridLines(true) //设置y轴坐标值是否需要画竖线
            it.setDrawLabels(true)//显示X轴坐标值
            it.setCenterAxisLabels(true)//设置坐标值居中显示，柱状图需要使用到
            it.textColor = resources.getColor(R.color.text_gray_99) //设置x轴文本颜色
            it.setDrawAxisLine(true) //设置是否绘制轴线
            it.axisLineColor = resources.getColor(R.color.color_line)//设置x轴线的颜色
            it.axisLineWidth = 2f//设置x轴线宽度
            it.granularity = 1f//设置X轴的间隔尺度,固定是1
            it.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    //需增加判断，要不会返回来会出现数组越界值情况，超过设置的最大最小值的范围
                    val size = timeList?.size ?: 0
                    if (value.toInt() >= size || value.toInt() < 0) {
                        return ""
                    }
                    return timeList?.get(value.toInt()) ?: ""
                }
            }
        }

        //Y轴
        _binding.lineChart.axisLeft.also {
            it.axisLineColor = resources.getColor(android.R.color.transparent)//设置Y轴线的颜色
            it.textColor = resources.getColor(R.color.text_gray_99) //设置Y轴文本颜色
            it.axisMinimum = 0f//设置坐标的最小值
            it.setDrawLabels(true)//显示Y轴坐标值
            it.setDrawAxisLine(false)//设置是否绘制轴线
            it.setDrawGridLines(true) //设置x轴坐标值是否需要画横线
            it.enableGridDashedLine(4f, 4f, 0f)//设置破折线
            it.gridLineWidth = 0.8f
            it.gridColor = Color.parseColor("#F2F2F2")
            it.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)//设置Y轴坐标值显示的位置
            it.spaceTop = 35f//设置Y轴最大值比数据的最大值大，不设置的话，数据的最大值就是Y轴的最大值
            it.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return Util.getDoubleText(value.toDouble())
                }
            }
        }


        //自定义图例图标
        _binding.flexboxChartLenged.removeAllViews()
        for (i in 0 until  data.getYDataList().size) {
            val chartLegend = ChartLegend(requireContext())
            chartLegend.setLabel(data.getYDataList()[i].getLegend().first)
            chartLegend.setCbstyle(data.getYDataList()[i].getLegend().second)
            _binding.flexboxChartLenged.addView(chartLegend)

        }

    }


    private fun setchartData(data: ChartListDataModel) {

        //设置highlight为空，刷新后不显示MarkerView
        _binding.lineChart.highlightValue(null)

        val timeList = data.getXTimeList()
        val chartYDataList = data.getYDataList()
        val lineData = LineData().also {
            it.isHighlightEnabled = true
        }
        var granularity: Float? = null//X轴数据间隔
        for (i in chartYDataList.indices) {
            //设置一条折线数据
            val lineDataValues = mutableListOf<Entry>()
            val chartYData = chartYDataList[i]//一条折线数据
            if (chartYData.getYDataList().isNullOrEmpty()) {
                continue
            }
            /*     for (yDataIndex in chartYData.getYDataList().indices) {
                     val time =
                         DateUtils.HH_mm_format.parse(timeList[yDataIndex]).time / MINUTES_INTERVAL
                     val y = chartYData.getYDataList()[yDataIndex]
                     lineDataValues.add(Entry(yDataIndex, y))
                 }*/

            for (x in timeList.indices) {
                val y = chartYData.getYDataList()[x]
                lineDataValues.add(Entry(x.toFloat(), y))
            }


            //X轴
            _binding.lineChart.xAxis.also {
                it.axisMaximum = lineDataValues[lineDataValues.size - 1].x//设置坐标的最大值
                it.axisMinimum = lineDataValues[0].x//设置坐标的最小值
            }
            val lineDataSet = LineDataSet(
                lineDataValues,
                chartYData.label
            ).also {
                val color = colors[i % colors.size]
                it.mode = LineDataSet.Mode.CUBIC_BEZIER
                it.setDrawCircles(false)//画原点
                it.setCircleColor(color.color)
                it.setDrawFilled(true)
                it.fillColor = color.alphaColor//设置fill区域的颜色
                it.fillAlpha = 100//设置fill区域的颜色的透明度
                it.highLightColor = resources.getColor(R.color.colorAccent)
                it.setDrawVerticalHighlightIndicator(true)
                it.setDrawHorizontalHighlightIndicator(false)
                it.enableDashedHighlightLine(4f, 4f, 0f)
                it.highlightLineWidth = 0.8f
                it.setDrawValues(false)//是否显示点的值
                it.color = color.color//设置曲线的颜色
            }
            if (granularity == null && lineDataValues.size > 2) {
                granularity = lineDataValues[1].x - lineDataValues[0].x
            }
            lineData.addDataSet(lineDataSet)
        }

        _binding.lineChart.xAxis.granularity = granularity ?: 5f//根据X轴的数据，设置坐标的间隔尺度

        //设置图例，数据种类颜色标识
        if ((data.dataList?.size ?: 0) > 1) {
            _binding.lineChart.legend.also {
                it.isEnabled = true//是否显示类型图例
                it.form = Legend.LegendForm.LINE//图标样式
                it.formLineWidth = 2f//线条宽度
                it.textSize = 11f
                it.textColor = resources.getColor(R.color.text_black)
                it.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM//位置位于底部
                it.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT//位置居左对齐
                it.orientation = Legend.LegendOrientation.HORIZONTAL//水平分布
                it.isWordWrapEnabled = true//开启自动换行
                it.xEntrySpace = 20f//设置左右间距
                it.yEntrySpace = 5f//设置上下间距
            }
        } else {
            _binding.lineChart.legend.isEnabled = false //是否显示类型图例
        }

        _binding.lineChart.data = lineData
        _binding.lineChart.invalidate()
        _binding.lineChart.animateXY(1000, 1000)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        LogUtil.i("liaojinsha", "LineChartFragment onDestroyView")
    }
}
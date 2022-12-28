package com.olp.weco.ui.chart

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter

import com.olp.weco.R
import com.olp.weco.base.BaseFragment
import com.olp.weco.databinding.FragmentBarChartBinding
import com.olp.weco.model.ChartColor
import com.olp.weco.ui.energy.chart.EnergyChartFragment
import com.olp.weco.ui.energy.chart.EnergyChartFragment.Companion.DATALIST_KEY
import com.olp.weco.ui.energy.chart.EnergyChartFragment.Companion.UNIT
import com.olp.weco.view.ChartLegend
import com.olp.lib.util.GsonManager
import com.olp.lib.util.LogUtil
import com.olp.lib.util.Util

/**
 * 柱状图表
 */
class BarChartFragment : BaseFragment() {


    private lateinit var _binding: FragmentBarChartBinding
    private var colors = arrayListOf<ChartColor>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        LogUtil.i("liaojinsha", "BarChartFragment onCreateView")
        _binding = FragmentBarChartBinding.inflate(inflater, container, false)

        arguments.apply {
            val datalist = this?.getString(DATALIST_KEY)
            val unit = this?.getString(UNIT)
            val chartListDataModel =
                datalist?.let { GsonManager.fromJson(it, ChartListDataModel::class.java) }
            colors = this?.getParcelableArrayList(EnergyChartFragment.COLORS) ?: arrayListOf(
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
        LogUtil.i("liaojinsha", "BarChartFragment刷新数据")
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


        _binding.barChart.also {
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


            _binding.barChart.marker = multipleChartMarkView

            multipleChartMarkView.chartView = it
        }

        //X轴
        _binding.barChart.xAxis.also {
            it.isEnabled = true //设置X轴启用
            it.position = XAxis.XAxisPosition.BOTTOM //设置X轴坐标值显示的位置
            it.setDrawGridLines(true) //设置y轴坐标值是否需要画竖线
            it.setDrawLabels(true)//显示X轴坐标值
//            it.setCenterAxisLabels(true)//设置坐标值居中显示，柱状图需要使用到
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
        _binding.barChart.axisLeft.also {
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
        for (i in 0 until data.getYDataList().size) {
            val chartLegend = ChartLegend(requireContext())
            chartLegend.setLabel(data.getYDataList()[i].getLegend().first)
            chartLegend.setCbstyle(data.getYDataList()[i].getLegend().second)
            _binding.flexboxChartLenged.addView(chartLegend)

        }


    }


    private fun setchartData(data: ChartListDataModel) {
        if (data.getXTimeList().isEmpty() || data.getYDataList().isEmpty()
        ) {
            _binding.barChart.data = null
            _binding.barChart.invalidate()
            return
        }
        //设置highlight为空，刷新后不显示MarkerView
        _binding.barChart.highlightValue(null)

        val timeList = data.getXTimeList()
        val chartYDataList = data.getYDataList()
        val barData = BarData().also {
            it.isHighlightEnabled = true
        }
        //数据源
        for (i in chartYDataList.indices) {
            //设置一种柱状图数据
            val barDataValues = mutableListOf<BarEntry>()
            val chartYData = chartYDataList[i]//一种柱状图数据
            if (chartYData.getYDataList().isEmpty()) {
                continue
            }
            for (yDataIndex in chartYData.getYDataList().indices) {
                val y = chartYData.getYDataList()[yDataIndex]
                barDataValues.add(BarEntry(yDataIndex.toFloat(), y))
            }

            val color = colors[i % colors.size]
            val barDataSet = BarDataSet(
                barDataValues,
                chartYData.label
            ).also {
                it.setDrawValues(false)//是否显示点的值
                it.color = color.color//设置柱状图的颜色
                it.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return Util.getDoubleText(value.toDouble())
                    }
                }
            }
            barData.addDataSet(barDataSet)
        }

        //设置图例，数据种类颜色标识
        if ((data.dataList?.size ?: 0) > 1) {
            _binding.barChart.legend.also {
                it.isEnabled = true//是否显示类型图标
                it.form = Legend.LegendForm.LINE//图例样式
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
            _binding.barChart.legend.isEnabled = false //是否显示类型图例
        }


        val groupWidth = 1f//设置一组柱状图宽度为1f,这里固定这样，无需修改
        //默认1条柱的值
        var groupSpace = 0.4f//组之间的间隔
        val barSpace = 0f //组内柱状图之间的间隔
        var barWidth = 0.6f // 柱状图宽度
        // 例如4条柱状图，(0.2 + 0) * 4 + 0.08 = 1.00 -> interval per "group",计算出一组柱状图的宽度大小
        val groupBarCount = barData.dataSetCount//一组柱状图的数量
        if (groupBarCount > 1) {
            barWidth = if (groupBarCount > 4) {
                0.1f
            } else {
                0.2f
            }
            groupSpace = groupWidth - (barWidth + barSpace) * groupBarCount
        }

        _binding.barChart.data = barData
        barData.barWidth = barWidth

        val startX = 0f
        val endX = startX + groupWidth * timeList.size
        //X轴
        _binding.barChart.xAxis.also {
            //包头不包尾
            it.axisMinimum = startX//设置坐标的最小值
            it.axisMaximum = endX//设置坐标的最大值
        }

        //设置图例，数据种类颜色标识
        if ((data.dataList?.size ?: 0) > 1) {
            _binding.barChart.groupBars(startX, groupSpace, barSpace)
        }
        _binding.barChart.invalidate()
        _binding.barChart.animateXY(1000, 1000)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        LogUtil.i("liaojinsha", "BarChartFragment onDestroyView")

    }
}
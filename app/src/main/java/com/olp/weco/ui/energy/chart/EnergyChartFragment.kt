package com.olp.weco.ui.energy.chart

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.olp.weco.R
import com.olp.weco.base.BaseFragment
import com.olp.weco.databinding.FragmentEnergyChartBinding
import com.olp.weco.model.ChartColor
import com.olp.weco.model.energy.ChartModel
import com.olp.weco.ui.chart.BarChartFragment
import com.olp.weco.ui.chart.LineChartFragment
import com.olp.weco.ui.common.model.DataType
import com.olp.lib.util.GsonManager
import com.olp.weco.ui.energy.viewmodel.EnergyViewModel

class EnergyChartFragment : BaseFragment() {

    companion object {

        const val DATALIST_KEY = "datalist"
        const val UNIT = "unit"
        const val COLORS = "colors"
        val arrayOf = arrayListOf(
            ChartColor(Color.parseColor("#F6F6F8"), Color.parseColor("#33F6F6F8")),
            ChartColor(Color.parseColor("#999999"), Color.parseColor("#33999999")),
            ChartColor(Color.parseColor("#80DA8A"), Color.parseColor("#3380DA8A")),
            ChartColor(Color.parseColor("#5E72E4"), Color.parseColor("#335E72E4")),
        )


    }


    private lateinit var _binding: FragmentEnergyChartBinding

    private val energyViewModel: EnergyViewModel by viewModels(ownerProducer = { requireParentFragment() })


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnergyChartBinding.inflate(inflater, container, false)
        initData()




        return _binding.root
    }


    private fun initData() {
        //初始化请求
        energyViewModel.stationLiveData.observe(viewLifecycleOwner) {
            val second = it.second
            if (second != null) {
                setTotalData(second)
            }
        }


    }


    private fun setTotalData(chartData: ChartModel) {/*
        val energyTotal = chartData.energyTotal
        val solarTotal = chartData.solarTotal
        val batTotal = chartData.batTotal
        val gridTotal = chartData.gridTotal
        val loadTotal = chartData.loadTotal

        _binding.chartViewVlude.tvBat.text = batTotal
        _binding.chartViewVlude.tvGrid.text = gridTotal
        _binding.chartViewVlude.tvPpv.text = solarTotal
        _binding.chartViewVlude.tvHome.text = loadTotal
        _binding.chartViewVlude.tvNetUse.text = energyTotal + "kWh"


        val dateType = energyViewModel.dateType
        val findFragment = this.childFragmentManager.findFragmentById(R.id.fcv_chart)
        if (dateType == DataType.DAY) {//显示曲线图
            if (findFragment!=null && findFragment is LineChartFragment){
                findFragment.refresh(energyViewModel.chartLiveData.value, "kW")
            } else {
                childFragmentManager.commit(true) {
                    val json = GsonManager.toJson(energyViewModel.chartLiveData.value)
                    val bundle = Bundle()
                    bundle.putString(DATALIST_KEY, json)
                    bundle.putString(UNIT, "kW")
                    bundle.putParcelableArrayList(COLORS, arrayOf)
                    val lineChartFragment = LineChartFragment()
                    lineChartFragment.arguments = bundle
                    replace(R.id.fcv_chart, lineChartFragment)
                }


            }


        } else {
            //刷新数据
            if (findFragment!=null && findFragment is BarChartFragment) {
                findFragment.refresh(energyViewModel.chartLiveData.value, "kW")
            } else {
                childFragmentManager.commit(true) {
                    val json = GsonManager.toJson(energyViewModel.chartLiveData.value)


                    val bundle = Bundle()
                    bundle.putString(DATALIST_KEY, json)
                    bundle.putString(UNIT, "kW")
                    bundle.putParcelableArrayList(COLORS, arrayOf)

                    val barChartFragment = BarChartFragment()
                    barChartFragment.arguments = bundle
                    replace(R.id.fcv_chart, barChartFragment)
                }

            }
        }*/

    }


}




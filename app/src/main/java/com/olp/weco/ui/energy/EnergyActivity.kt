package com.olp.weco.ui.energy

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.viewModels
import androidx.fragment.app.commit
import com.olp.lib.util.GsonManager
import com.olp.weco.R
import com.olp.weco.base.BaseActivity
import com.olp.weco.databinding.ActivityEnergyBinding
import com.olp.weco.model.ChartColor
import com.olp.weco.model.energy.ChartModel
import com.olp.weco.ui.chart.BarChartFragment
import com.olp.weco.ui.chart.LineChartFragment
import com.olp.weco.ui.common.model.DataType
import com.olp.weco.ui.energy.chart.EnergyChartFragment
import com.olp.weco.ui.energy.viewmodel.EnergyViewModel
import com.olp.weco.view.DateSelectView
import com.olp.weco.view.OnTabSelectedListener
import com.olp.weco.view.Tab
import com.olp.weco.view.pop.ListPopuwindow
import com.olp.weco.view.popuwindow.ListPopModel
import java.util.*

class EnergyActivity : BaseActivity(), OnClickListener {


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

        fun start(context: Context, plantId: String) {
            val intent = Intent(context, EnergyActivity::class.java)
            intent.putExtra("plantId", plantId)
            context.startActivity(intent)
        }
    }


    private lateinit var _binding: ActivityEnergyBinding


    private val energyViewModel: EnergyViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEnergyBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        setliseners()
        initViews()
        initData()
    }


    private fun setliseners() {
        _binding.title.setOnLeftIconClickListener {
            finish()
        }
        _binding.date.tvDateType.setOnClickListener(this)

        _binding.tabLayout.addTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelect(selectTab: Tab, selectPosition: Int) {
                energyViewModel.dateType = selectPosition + 1
            }

        })
    }


    private fun initViews() {
        _binding.title.setTitleText(getString(R.string.m17_energy))
        //初始化时间 年月日
        setDate()
        _binding.date.dataSelectView.setListener(object : DateSelectView.OntimeselectedListener {
            override fun onDateSelectedListener(date: Date) {
                //1.日期改变时
                energyViewModel.setTime(date)
                //2.重新请求数据
                getPlantData()
            }
        })
        //绑定图表视图
        supportFragmentManager.commit {
            add(R.id.chart_container, EnergyChartFragment())
        }
        _binding.date.tvDateType.text = DataType.getDateNameByType(energyViewModel.dateType)


    }


    private fun initData() {

        energyViewModel.stationLiveData.observe(this) {
            val second = it.second
            if (second != null) {
                setTotalData(second)
            }
        }


        //初始化请求
        energyViewModel.currentPlantId = intent.getStringExtra("plantId").toString()
        //1.开始请求数据
        getPlantData()

    }


    private fun setTotalData(chartData: ChartModel) {
        val lowEnergy = chartData.lowEnergy
        val avgEnergy = chartData.avgEnergy
        val highEnergy = chartData.highEnergy
        val totalEnergy = chartData.totalEnergy




        _binding.tvLowEnergy.text = lowEnergy
        _binding.tvAvgEnergy.text = avgEnergy
        _binding.tvHighEnergy.text = highEnergy
        _binding.tvNetUse.text = totalEnergy + "kWh"


        val dateType = energyViewModel.dateType
        val findFragment = this.supportFragmentManager.findFragmentById(R.id.fcv_chart)
        if (dateType == DataType.DAY) {//显示曲线图
            if (findFragment != null && findFragment is LineChartFragment) {
                findFragment.refresh(energyViewModel.chartLiveData.value, "kW")
            } else {
                supportFragmentManager.commit(true) {
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
            if (findFragment != null && findFragment is BarChartFragment) {
                findFragment.refresh(energyViewModel.chartLiveData.value, "kW")
            } else {
                supportFragmentManager.commit(true) {
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
        }

    }


    private fun setDate() {
        energyViewModel.setTime(_binding.date.dataSelectView.nowDate)
    }


    override fun onClick(v: View?) {
        when {
            v === _binding.date.tvDateType -> {
                //选择年月日
                showDateChoose()
            }

        }
    }


    private fun showDateChoose() {
        val date = listOf(
            getString(R.string.m89_total),
            getString(R.string.m72_year),
            getString(R.string.m71_month),
            getString(R.string.m70_day)
        )

        val options = mutableListOf<ListPopModel>()
        date.forEach {
            options.add(ListPopModel(it, false))
        }
        ListPopuwindow.showPop(
            this,
            options,
            _binding.date.tvDateType,
            ""
        ) {
            //根据日期请求图表数据
            _binding.date.tvDateType.text = options[it].title
            energyViewModel.dateType = it

            //重新请求求数据
            getPlantData()
        }
    }


    /**
     * 请求数据
     */
    private fun getPlantData() {

        //1.请求图表数据
        energyViewModel.getPlantChartData()
        //3.设置日期
        _binding.date.dataSelectView.setDateType(energyViewModel.dateType)

    }


}
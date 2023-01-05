package com.olp.weco.ui.energy

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.StyleSpan
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
import com.olp.weco.model.energy.DayChartModel
import com.olp.weco.ui.chart.BarChartFragment
import com.olp.weco.ui.chart.LineChartFragment
import com.olp.weco.ui.common.model.DataType
import com.olp.weco.ui.energy.chart.EnergyChartFragment
import com.olp.weco.ui.energy.viewmodel.EnergyViewModel
import com.olp.weco.utils.ValueUtil
import com.olp.weco.view.DateSelectView
import com.olp.weco.view.OnTabSelectedListener
import com.olp.weco.view.Tab
import com.olp.weco.view.TextTab
import com.olp.weco.view.dialog.OptionsDialog
import com.olp.weco.view.pop.ListPopuwindow
import com.olp.weco.view.popuwindow.ListPopModel
import java.util.*

class EnergyActivity : BaseActivity(), OnClickListener {


    companion object {

        const val DATALIST_KEY = "datalist"
        const val UNIT = "unit"
        const val COLORS = "colors"
        val arrayOf = arrayListOf(
            ChartColor(Color.parseColor("#3321FF"), Color.parseColor("#333321FF")),
            ChartColor(Color.parseColor("#FFAA05"), Color.parseColor("#33FFAA05")),
            ChartColor(Color.parseColor("#333333"), Color.parseColor("#33333333")),
            ChartColor(Color.parseColor("#06C854"), Color.parseColor("#3306C854")),
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
                energyViewModel.energyType = selectPosition + 1
                //2.重新请求数据
                getPlantData()
            }

            override fun onTabSelect(selectTab: TextTab, selectPosition: Int) {
            }

        })
    }


    private fun initViews() {
        _binding.tabLayout.setSelectTabPosition(0,false)

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
/*        //绑定图表视图
        supportFragmentManager.commit {
            add(R.id.chart_container, EnergyChartFragment())
        }*/
        _binding.date.tvDateType.text = DataType.getDateNameByType(energyViewModel.dateType)


    }


    private fun initData() {

        energyViewModel.stationLiveData.observe(this) {
            val second = it.second
            if (second != null) {
                setTotalData(second)
            }
        }


        energyViewModel.stationDayLiveData.observe(this){
            val second = it.second
            if (second != null) {
                setDayData(second)
            }
        }


        //初始化请求
        energyViewModel.currentPlantId = intent.getStringExtra("plantId").toString()
        //1.开始请求数据
        getPlantData()

    }


    private fun setTotalData(chartData: ChartModel) {
        val lowEnergy = chartData.lowEnergy?:"0"
        val avgEnergy = chartData.avgEnergy?:"0"
        val highEnergy = chartData.highEnergy?:"0"
        val totalEnergy = chartData.totalEnergy?:"0"


        ValueUtil.valueFromKWh(lowEnergy.toDouble()).apply {
            val s = first + second
            _binding.tvLowEnergy.text =s
        }

        ValueUtil.valueFromKWh(avgEnergy.toDouble()).apply {
            val s = first + second
            _binding.tvAvgEnergy.text =s
        }


        ValueUtil.valueFromKWh(highEnergy.toDouble()).apply {
            val s = first + second
            _binding.tvHighEnergy.text =s
        }

        ValueUtil.valueFromKWh(totalEnergy.toDouble()).apply {
            val s = first + second
            _binding.tvNetUse.text =s
        }


        val findFragment = this.supportFragmentManager.findFragmentById(R.id.fcv_chart)
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



    private fun setDayData(chartData: DayChartModel) {
        val lowEnergy = chartData.lowPower?:"0"
        val avgEnergy = chartData.avgPower?:"0"
        val highEnergy = chartData.highPower?:"0"
        val totalEnergy = chartData.energy?:"0"


        ValueUtil.valueFromKWh(lowEnergy.toDouble()).apply {
            val s = first + second
            _binding.tvLowEnergy.text =s
        }

        ValueUtil.valueFromKWh(avgEnergy.toDouble()).apply {
            val s = first + second
            _binding.tvAvgEnergy.text =s
        }


        ValueUtil.valueFromKWh(highEnergy.toDouble()).apply {
            val s = first + second
            _binding.tvHighEnergy.text =s
        }

        ValueUtil.valueFromKWh(totalEnergy.toDouble()).apply {
            val s = first + second
            _binding.tvNetUse.text =s
        }


        val findFragment = this.supportFragmentManager.findFragmentById(R.id.fcv_chart)
        //显示曲线图
        if (findFragment != null && findFragment is LineChartFragment) {
            findFragment.refresh(energyViewModel.chartLiveData.value, "W")
        } else {
            supportFragmentManager.commit(true) {
                val json = GsonManager.toJson(energyViewModel.chartLiveData.value)
                val bundle = Bundle()
                bundle.putString(DATALIST_KEY, json)
                bundle.putString(UNIT, "W")
                bundle.putParcelableArrayList(COLORS, arrayOf)
                val lineChartFragment = LineChartFragment()
                lineChartFragment.arguments = bundle
                replace(R.id.fcv_chart, lineChartFragment)
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

        OptionsDialog.show(supportFragmentManager, date.toTypedArray()) {
            //根据日期请求图表数据
            _binding.date.tvDateType.text = date[it]
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
        if (energyViewModel.dateType==DataType.DAY){
            energyViewModel.getDayChartData()
        }else{
            energyViewModel.getPlantChartData()
        }
        //3.设置日期
        _binding.date.dataSelectView.setDateType(energyViewModel.dateType)

    }


}
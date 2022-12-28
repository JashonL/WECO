package com.olp.weco.ui.impact

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.TextUtils.replace
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.adapters.CalendarViewBindingAdapter.setDate
import androidx.fragment.app.commit
import com.olp.lib.util.GsonManager
import com.olp.weco.R
import com.olp.weco.base.BaseActivity
import com.olp.weco.databinding.ActivityImpactBinding
import com.olp.weco.model.ChartColor
import com.olp.weco.ui.chart.BarChartFragment
import com.olp.weco.ui.chart.LineChartFragment
import com.olp.weco.ui.common.model.DataType
import com.olp.weco.ui.energy.chart.EnergyChartFragment
import com.olp.weco.ui.energy.viewmodel.EnergyViewModel
import com.olp.weco.ui.impact.viewmodel.ImpactViewModel
import com.olp.weco.utils.ValueUtil
import com.olp.weco.view.DateSelectView
import com.olp.weco.view.pop.ListPopuwindow
import com.olp.weco.view.popuwindow.ListPopModel
import java.util.*
import kotlin.math.roundToInt

class ImpactActivity : BaseActivity(), View.OnClickListener {


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
            val intent = Intent(context, ImpactActivity::class.java)
            intent.putExtra("plantId", plantId)
            context.startActivity(intent)
        }
    }


    private lateinit var _binding: ActivityImpactBinding

    private val energyViewModel: ImpactViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityImpactBinding.inflate(layoutInflater)
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

        _binding.date.dataSelectView.setListener(object : DateSelectView.OntimeselectedListener {
            override fun onDateSelectedListener(date: Date) {
                //1.日期改变时
                energyViewModel.setTime(date)
                //2.重新请求数据
                getPlantData()
            }
        })

    }


    private fun initViews() {
        _binding.title.setTitleText(getString(R.string.m85_impact))
        //初始化时间 年月日
        setDate()
/*        //绑定图表视图
        supportFragmentManager.commit {
            add(R.id.chart_container, EnergyChartFragment())
        }*/
        _binding.date.tvDateType.text = DataType.getDateNameByType(energyViewModel.dateType)


    }


    private fun initData() {

        val dateType = energyViewModel.dateType


        energyViewModel.impactLiveData.observe(this) {


            //实时收益
            _binding.tvCurrentValue.text = it?.saveCost


            //当前时间的总收益
            _binding.tvAllImpact.text = it?.saveTotalCost


            //设置收益标题
            _binding.tvDateTitle.text = when (dateType) {
                DataType.DAY -> {
                    getString(R.string.m150_this_day)
                }
                DataType.MONTH -> {
                    getString(R.string.m149_this_month)
                }
                DataType.YEAR -> {
                    getString(R.string.m148_this_year)
                }
                else -> {
                    ""
                }
            }

            //根据数据动态设置SOLAR 和Load的高度
            _binding.llOffset.tvPercenter.text = it?.pvLoadPerce + "%"


            val solarEle = it?.pvEnergy ?: "0.0"
            val loadEle = it?.loadEnergy ?: "0.0"



            ValueUtil.valueFromKWh(solarEle.toDouble()).apply {
                val s = first + second
                val sb = SpannableStringBuilder(s).append(" ").append(getString(R.string.m9_solar)).apply {
                    setSpan(
                        StyleSpan(Typeface.BOLD),
                        this.indexOf(s), this.indexOf(s) + s.length,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE
                    )
                }
                _binding.llOffset.tvSolar.text = sb
            }

            ValueUtil.valueFromKWh(loadEle.toDouble()).apply {
                val s = first + second
                val sb = SpannableStringBuilder(s).append(" ").append(getString(R.string.m32_load)).apply {
                    setSpan(
                        StyleSpan(Typeface.BOLD),
                        this.indexOf(s), this.indexOf(s) + s.length,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE
                    )
                }
                _binding.llOffset.tvLoad.text = sb
            }

            //动态设置vSolar的高度
            val loadHigh = resources.getDimensionPixelOffset(R.dimen.dp_200)

            if (loadEle.toDouble() != 0.0) {
                val layoutParams = _binding.llOffset.vSolar.layoutParams
                layoutParams.height =
                    (loadHigh * (solarEle.toDouble() / loadEle.toDouble())).roundToInt()
                _binding.llOffset.vSolar.layoutParams = layoutParams
            }


        }


        val findFragment = this.supportFragmentManager.findFragmentById(R.id.fcv_chart)
        energyViewModel.impactChartLiveData.observe(this) {
            //设置图表
            if (dateType == DataType.DAY) {//线性图表
                if (findFragment != null && findFragment is LineChartFragment) {
                    findFragment.refresh(it, "kW")
                } else {
                    supportFragmentManager.commit(true) {
                        val json = GsonManager.toJson(it)
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
                //柱状图
                //刷新数据
                if (findFragment != null && findFragment is BarChartFragment) {
                    findFragment.refresh(it, "kW")
                } else {
                    supportFragmentManager.commit(true) {
                        val json = GsonManager.toJson(it)


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


        //初始化请求
        energyViewModel.currentPlantId = intent.getStringExtra("plantId").toString()
        //1.开始请求数据
        getPlantData()

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

        }
    }


    /**
     * 请求收益数据
     */
    private fun getPlantData() {

        //1.请求图表数据
        energyViewModel.getPlantImpactData()
        //3.设置日期
        _binding.date.dataSelectView.setDateType(energyViewModel.dateType)

    }


}
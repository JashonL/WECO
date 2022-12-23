package com.olp.weco.ui.energy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.olp.weco.R
import com.olp.weco.base.BaseFragment
import com.olp.weco.databinding.FragmentEnergyBinding
import com.olp.weco.model.PlantModel
import com.olp.weco.ui.common.model.DataType
import com.olp.weco.ui.energy.chart.EnergyChartFragment
import com.olp.weco.ui.energy.impact.ImpactFragment
import com.olp.weco.ui.station.viewmodel.StationViewModel
import com.olp.weco.view.DateSelectView
import com.olp.weco.view.pop.ListPopuwindow
import com.olp.weco.view.popuwindow.ListPopModel
import com.olp.lib.util.gone
import com.olp.lib.util.visible
import java.util.*

class EnergyFragment : BaseFragment(), View.OnClickListener {

    private lateinit var _binding: FragmentEnergyBinding

    private val viewModel: StationViewModel by viewModels()

    private val energyViewModel: EnergyViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnergyBinding.inflate(inflater, container, false)
        setliseners()
        initViews()
        initData()
        return _binding.root
    }

    private fun initViews() {
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
        childFragmentManager.commit {
            add(R.id.chart_container, EnergyChartFragment())
        }
        _binding.date.tvDateType.text = DataType.getDateNameByType(energyViewModel.dateType)


        //绑定收益视图
        childFragmentManager.commit {
            add(R.id.consumption_container, ImpactFragment())
        }

    }


    private fun initData() {
        //初始化请求
        viewModel.getPlantListLiveData.observe(viewLifecycleOwner) {
            _binding.srlEmptyview.finishRefresh()
            if (it.first) {
                val second = it.second
                if (second == null || second.isEmpty()) {//没有电站  显示空
                    _binding.header.tvTitle.text = ""

                    _binding.srlRefresh.visible()
                    _binding.srlEmptyview.gone()


                } else {
                    _binding.srlRefresh.visible()
                    _binding.srlEmptyview.gone()

                    //默认选中第一个电站
                    _binding.header.tvTitle.text = second[0].stationName
                    energyViewModel.currentStation = second[0]

                    //1.开始请求数据
                    getPlantData()

                }
            }
        }






        fetchPlantList()
    }


    private fun setDate() {
        energyViewModel.setTime(_binding.date.dataSelectView.nowDate)
    }


    private fun setliseners() {
        _binding.header.tvTitle.setOnClickListener(this)
        _binding.date.tvDateType.setOnClickListener(this)
        _binding.srlEmptyview.setOnRefreshListener {
            fetchPlantList()
        }
    }


    private fun fetchPlantList() {
        showDialog()
        viewModel.getPlantList()
    }

    override fun onClick(p0: View?) {
        when {
            p0 === _binding.header.tvTitle -> {
                showPlantList()
            }

            p0 === _binding.date.tvDateType -> {
                //选择年月日
                showDateChoose()
            }


        }
    }

    private fun showPlantList() {
        val second = viewModel.getPlantListLiveData.value?.second
        if (second == null || second.isEmpty()) {
            fetchPlantList()
        } else {
            val options = mutableListOf<ListPopModel>()
            for (plant in second) {
                options.add(ListPopModel(plant.stationName, false))
            }

            val curItem: String? = if (viewModel.currentStation != null) {
                viewModel.currentStation!!.id
            } else {
                ""
            }
            context?.let {
                ListPopuwindow.showPop(
                    it,
                    options,
                    _binding.header.tvTitle,
                    curItem.toString()
                ) {
                    /*  _binding.header.tvTitle.text = options[it].title
                      //选择电站
                      energyViewModel.currentStation = second[it]
                      //重新请求数据
                      getPlantData()*/

                    showPlantData(second[it])

                }
            }
        }
    }


    private fun showPlantData(station: PlantModel) {
        _binding.header.tvTitle.text = station.stationName

        //选择电站
        energyViewModel.currentStation = station
        //重新请求数据
        getPlantData()


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

        context?.let {
            ListPopuwindow.showPop(
                it,
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
    }


    /**
     * 请求数据
     */
    private fun getPlantData() {

        //1.请求图表数据
        energyViewModel.getPlantChartData()
        //2.请求收益 和二氧化碳
        energyViewModel.getPlantImpactData()
        //3.设置日期
        _binding.date.dataSelectView.setDateType(energyViewModel.dateType)

    }


}
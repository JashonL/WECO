package com.olp.weco.ui.energy.impact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.olp.weco.R
import com.olp.weco.base.BaseFragment
import com.olp.weco.databinding.FragmentImpactBinding
import com.olp.weco.ui.chart.BarChartFragment
import com.olp.weco.ui.common.model.DataType
import com.olp.weco.ui.energy.EnergyViewModel
import com.olp.lib.util.gone
import com.olp.lib.util.visible

class ImpactFragment : BaseFragment() {

    private val energyViewModel: EnergyViewModel by viewModels(ownerProducer = { requireParentFragment() })


    private lateinit var _binding: FragmentImpactBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImpactBinding.inflate(inflater, container, false)
        initviews()
        initData()
        return _binding.root
    }

    private fun initviews() {
        //绑定图表
        //绑定收益视图
        childFragmentManager.commit {
            add(R.id.impact_chart, BarChartFragment())
        }

    }


    private fun initData() {
        //初始化请求
        energyViewModel.impactLiveData.observe(viewLifecycleOwner) {
        }

        val dateType = energyViewModel.dateType
        energyViewModel.impactChartLiveData.observe(viewLifecycleOwner) {
            //设置图表
            if (dateType == DataType.DAY) {//隐藏图表
                _binding.impactChart.gone()
                _binding.llToday.visible()

            } else {
                _binding.impactChart.visible()
                _binding.llToday.gone()
                //设置图表


            }

        }


    }


}
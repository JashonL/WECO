package com.olp.weco.ui.home.pv

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.olp.weco.R
import com.olp.weco.base.BaseFragment
import com.olp.weco.databinding.FragmentSystemInvStatusBinding
import com.olp.weco.ui.home.pv.viewmodel.PvStationViewmodel
import com.olp.weco.utils.ValueUtil
import kotlinx.coroutines.delay

class PvStatusFragment : BaseFragment() {


    private lateinit var _binding: FragmentSystemInvStatusBinding


    private val viewModel: PvStationViewmodel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val arguments = arguments
        viewModel.stationId = arguments?.let {
            val stationId = it.getString("stationId")
            stationId
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSystemInvStatusBinding.inflate(inflater, container, false)
        initData()
        return _binding.root
    }


    private fun initData() {
        viewModel.statusLiveData.observe(viewLifecycleOwner) {
            _binding.srlRefresh.finishRefresh()

            ValueUtil.valueFromW(it?.solar?.toDouble()).apply {
                _binding.tvSolarPower.text = String.format("%s%s", first, second)
            }
            ValueUtil.valueFromW(it?.grid?.toDouble()).apply {
                _binding.tvGridPower.text = String.format("%s%s", first, second)
            }
            ValueUtil.valueFromW(it?.home?.toDouble()).apply {
                _binding.tvHomePower.text = String.format("%s%s", first, second)
            }

            ValueUtil.valueFromW(it?.power?.toDouble()).apply {
                _binding.tvPower.text = String.format("%s%s", first, second)
            }
            ValueUtil.valueFromW(it?.energyToday?.toDouble()).apply {
                _binding.tvEnergyToday.text = String.format("%s%s", first, second)
            }
            ValueUtil.valueFromW(it?.energyTotal?.toDouble()).apply {
                _binding.tvEnergyToday.text = String.format("%s%s", first, second)
            }


            val onlineStatus = it?.onlineStatus
            if ("1" == onlineStatus){
                _binding.ivStatus.setImageResource(R.drawable.check_normal)
                _binding.tvStatus.text = getString(R.string.m82_system_nomal)
            }else{
                _binding.ivStatus.setImageResource(R.drawable.exception)
                _binding.tvStatus.text = getString(R.string.m83_system_exception)

            }

        }

        //获取数据
        viewModel.getDataOverview()

        //开启定时刷新
        timerStart()
    }


    fun getDataByStationId(id: String) {
        viewModel.stationId = id
        //获取数据
        viewModel.getDataOverview()
    }



    //启动定时任务
    private fun timerStart() {
        lifecycleScope.launchWhenResumed {
            repeat(Int.MAX_VALUE) {
                //获取数据
                viewModel.getDataOverview()
                delay(15 * 1000)
            }
        }
    }


}
package com.olp.weco.ui.home.storage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.olp.lib.util.gone
import com.olp.lib.util.setDrawableStart
import com.olp.lib.util.visible
import com.olp.weco.R
import com.olp.weco.base.BaseFragment
import com.olp.weco.databinding.FragmentSystemStatusBinding
import com.olp.weco.ui.device.activity.DeviceListActivity
import com.olp.weco.ui.energy.EnergyActivity
import com.olp.weco.ui.home.model.StorageModel
import com.olp.weco.ui.impact.ImpactActivity
import com.olp.weco.ui.station.viewmodel.StationViewModel
import com.olp.weco.utils.ValueUtil
import kotlinx.coroutines.delay

class HomeStatusFragment : BaseFragment(), OnClickListener {


    private lateinit var _binding: FragmentSystemStatusBinding


    private val viewModel: StorageViewmodel by viewModels()

    private val stationViewModel: StationViewModel by activityViewModels()

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
        _binding = FragmentSystemStatusBinding.inflate(inflater, container, false)
        initData()
        initlisteners()
        return _binding.root
    }

    private fun initlisteners() {
        _binding.llOther.llEnergy.setOnClickListener(this)
        _binding.llOther.llImpact.setOnClickListener(this)
        _binding.llOther.llDeviceList.setOnClickListener(this)

        _binding.srlRefresh.setOnRefreshListener {
            stationViewModel.getPlantList()
        }
    }


    private fun initData() {
        viewModel.statusLiveData.observe(viewLifecycleOwner) {
            _binding.srlRefresh.finishRefresh()

            val deviceCount = it?.deviceCount ?: "0"

            if ("0" == deviceCount) {
                showEmpty()
            } else {
                showSystem(it)
            }
        }

        //获取数据
//        viewModel.getDataOverview()

        //开启定时刷新
        timerStart()
    }

    private fun showSystem(it: StorageModel?) {
        showSys()

        val gridpower = it?.elcGridPower?.toDouble()
        val loadPower = it?.loadPower?.toDouble()
        val pvPower = it?.pvPower?.toDouble()
        val batteryPower = it?.batteryPower?.toDouble()


        ValueUtil.valueFromW(gridpower).apply {
            _binding.tvGridPower.text = String.format("%s%s", first, second)
        }
        ValueUtil.valueFromW(loadPower).apply {
            _binding.tvHomePower.text = String.format("%s%s", first, second)
        }
        ValueUtil.valueFromW(pvPower).apply {
            _binding.tvSolarPower.text = String.format("%s%s", first, second)
        }
        ValueUtil.valueFromW(batteryPower).apply {
            _binding.tvBatPower.text = String.format("%s%s", first, second)
        }


        //根据值的大小设置图片
        if (gridpower != null && gridpower != 0.0) {
            _binding.ivSysGrid.setImageResource(R.drawable.system_grid_blue)
        } else {
            _binding.ivSysGrid.setImageResource(R.drawable.system_grid)
        }


        if (loadPower != null && loadPower != 0.0) {
            _binding.ivSysHome.setImageResource(R.drawable.system_home_blue)
        } else {
            _binding.ivSysHome.setImageResource(R.drawable.system_home)
        }

        if (batteryPower != null && batteryPower != 0.0) {
            _binding.ivSysBat.setImageResource(R.drawable.system_bat_blue)
        } else {
            _binding.ivSysBat.setImageResource(R.drawable.system_bat)
        }

        if (pvPower != null && pvPower != 0.0) {
            _binding.ivSysPpv.setImageResource(R.drawable.system_ppv_blue)
        } else {
            _binding.ivSysPpv.setImageResource(R.drawable.system_ppv)
        }




        val onlineStatus = it?.plantStatus
        if ("1" == onlineStatus) {
            _binding.tvOlStatus.setDrawableStart(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.wifi_normal
                )
            )
            _binding.tvOlStatus.text = getString(R.string.m124_on_line)
        } else {
            _binding.tvOlStatus.setDrawableStart(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.wifi_offline
                )
            )
            _binding.tvOlStatus.text = getString(R.string.m125_off_line)
        }


        val animleft = AnimationUtils.loadAnimation(context, R.anim.system_left_anim)
        val animtop = AnimationUtils.loadAnimation(context, R.anim.system_top_anim)
        val animright = AnimationUtils.loadAnimation(context, R.anim.system_right_anim)
        val animbottom = AnimationUtils.loadAnimation(context, R.anim.system_bottom_anim)


        //设置动画
        if (gridpower != null) {
            if (gridpower.toDouble() > 0) {
                _binding.ivLeftAnim.visible()
                _binding.ivLeftAnim.setImageResource(R.drawable.left_anim)
                _binding.ivLeftAnim.startAnimation(animleft)
            } else if (gridpower.toDouble() == 0.0) {//隐藏动画
                _binding.ivLeftAnim.gone()
            } else {
                _binding.ivLeftAnim.visible()
                _binding.ivLeftAnim.setImageResource(R.drawable.right_anim)
                _binding.ivLeftAnim.startAnimation(animright)
            }
        }



        if (loadPower != null) {
            if (loadPower.toDouble() > 0) {
                _binding.ivRightAnim.visible()
                _binding.ivRightAnim.startAnimation(animright)
            } else {//隐藏动画
                _binding.ivRightAnim.gone()
            }
        }

        if (pvPower != null) {
            if (pvPower.toDouble() > 0) {
                _binding.ivTopAnim.visible()
                _binding.ivTopAnim.startAnimation(animtop)
            } else {//隐藏动画
                _binding.ivTopAnim.gone()
            }
        }

        if (batteryPower != null) {
            if (batteryPower.toDouble() > 0) {
                _binding.ivBottomAnim.visible()
                _binding.ivBottomAnim.setImageResource(R.drawable.top_anim)
                _binding.ivBottomAnim.startAnimation(animtop)
            } else if (batteryPower.toDouble() == 0.0) {//隐藏动画
                _binding.ivBottomAnim.gone()
            } else {
                _binding.ivBottomAnim.visible()
                _binding.ivBottomAnim.setImageResource(R.drawable.bottom_anim)
                _binding.ivBottomAnim.startAnimation(animbottom)
            }
        }


        //设置其他数据
        val pvDayChargeTotal = it?.pvDayChargeTotal ?: "0"
        val selfElc = it?.selfElc ?: "0"
        val deviceCount = it?.deviceCount ?: "0"

        _binding.llOther.tvImpactData.text = getString(R.string.m128_generated_today, selfElc)
        _binding.llOther.tvEnergyData.text =
            getString(R.string.m129_self_power_today, pvDayChargeTotal)
        _binding.llOther.tvDeviceData.text = getString(
            R.string.m160_device_are_run,
            deviceCount
        )
    }


    //启动定时任务
    private fun timerStart() {
        lifecycleScope.launchWhenResumed {
            repeat(Int.MAX_VALUE) {
                //获取数据
                if (!viewModel.stationId.isNullOrEmpty()) {
                    viewModel.getDataOverview()
                }
                delay(15 * 1000)
            }
        }
    }


    fun getDataByStationId(id: String) {
        viewModel.stationId = id
        //获取数据
        viewModel.getDataOverview()
    }


    fun showEmpty() {
        _binding.emptyView.emptyView.visible()
        _binding.clSys.gone()

        _binding.llOther.tvEnergyData.text = ""
        _binding.llOther.tvDeviceData.text = ""
        _binding.llOther.tvImpactData.text = ""
    }


    fun showSys() {
        _binding.emptyView.emptyView.gone()
        _binding.clSys.visible()
    }

    override fun onClick(v: View?) {
        when {
            v === _binding.llOther.llEnergy -> {
                viewModel.stationId?.let { EnergyActivity.start(requireActivity(), it) }
            }

            v === _binding.llOther.llImpact -> {
                viewModel.stationId?.let { ImpactActivity.start(requireActivity(), it) }
            }

            v === _binding.llOther.llDeviceList -> {
                viewModel.stationId?.let { DeviceListActivity.start(requireActivity(), it) }
            }


        }
    }


}
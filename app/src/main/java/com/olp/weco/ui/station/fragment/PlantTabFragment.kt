package com.olp.weco.ui.station.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.olp.weco.ui.station.monitor.PlantTabSwitchMonitor
import com.olp.weco.R
import com.olp.weco.base.BaseFragment
import com.olp.weco.databinding.FragmentPlantTabBinding
import com.olp.weco.model.PlantModel
import com.olp.weco.model.PlantStatusNumModel

/**
 * 电站列表TAB
 */
class PlantTabFragment : BaseFragment(),
    OnPlantStatusNumChangeListener {


    private var _binding: FragmentPlantTabBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlantTabBinding.inflate(inflater, container, false)
        initView()
        setListener()
        return binding.root
    }

    private fun refreshPlantStatusNum(plantStatusNumModel: PlantStatusNumModel) {
        binding.tabLayout.setTabText(
            getString(R.string.m40_all_format, plantStatusNumModel.allNum),
            0
        )
        binding.tabLayout.setTabText(
            getString(
                R.string.m41_online_format,
                plantStatusNumModel.onlineNum
            ), 1
        )
        binding.tabLayout.setTabText(
            getString(
                R.string.m42_offline_format,
                plantStatusNumModel.offline
            ), 2
        )
        binding.tabLayout.setTabText(
            getString(
                R.string.m43_trouble_format,
                plantStatusNumModel.faultNum
            ), 3
        )
    }

    private fun initView() {
        binding.tabLayout.setSelectTabPosition(0, false)
        binding.vpPlant.adapter = Adapter(this)
        binding.tabLayout.setupWithViewPager2(binding.vpPlant)
        binding.vpPlant.offscreenPageLimit = binding.vpPlant.childCount

        refreshPlantStatusNum(PlantStatusNumModel())
    }

    private fun setListener() {
        PlantTabSwitchMonitor.watch(viewLifecycleOwner.lifecycle) { _, position ->
            binding.tabLayout.setSelectTabPosition(position, false)
        }
    }

    inner class Adapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        private val fragments = arrayOf(
            PlantListFragment().also {
                val bundle = Bundle()
                bundle.putInt("status", PlantModel.PLANT_STATUS_ALL)
                it.arguments = bundle
            },
            PlantListFragment().also {
                val bundle = Bundle()
                bundle.putInt("status", PlantModel.PLANT_STATUS_ONLINE)
                it.arguments = bundle

            },
            PlantListFragment().also {
                val bundle = Bundle()
                bundle.putInt("status", PlantModel.PLANT_STATUS_OFFLINE)
                it.arguments = bundle
            },
            PlantListFragment().also {
                val bundle = Bundle()
                bundle.putInt("status", PlantModel.PLANT_STATUS_TROUBLE)
                it.arguments = bundle
            }
        )

        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }

    override fun onPlantStatusNumChange(plantStatusNumModel: PlantStatusNumModel) {
        refreshPlantStatusNum(plantStatusNumModel)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

interface OnPlantStatusNumChangeListener {
    fun onPlantStatusNumChange(plantStatusNumModel: PlantStatusNumModel)
}
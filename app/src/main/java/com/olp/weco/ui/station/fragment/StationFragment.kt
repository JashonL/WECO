package com.olp.weco.ui.station.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.olp.weco.databinding.FragmentHomePlantListBinding
import com.olp.weco.ui.home.viewmodel.PlantFilterViewModel
import com.olp.weco.ui.station.activity.AddPlantActivity
import com.olp.weco.ui.station.activity.AddTtchPlantActivity
import com.olp.weco.ui.station.viewmodel.PlantFilterModel

class StationFragment :Fragment(),OnClickListener{

    private lateinit var _binding: FragmentHomePlantListBinding

    private val filterViewModel: PlantFilterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomePlantListBinding.inflate(inflater, container, false)
        initData()
        initLisenter()
        return _binding.root
    }

    private fun initLisenter() {
        _binding.title.setOnRightImageClickListener {
            AddTtchPlantActivity.start(requireContext())
        }

    }


    private fun initData() {
        filterViewModel.setFilterType(PlantFilterModel.getDefaultFilter().filterType)
    }

    override fun onClick(v: View?) {


    }
}
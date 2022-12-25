package com.olp.weco.ui.home

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.findFragment
import androidx.fragment.app.viewModels
import com.olp.weco.ui.common.fragment.RequestPermissionHub
import com.olp.weco.R
import com.olp.weco.base.BaseFragment
import com.olp.weco.databinding.FragmentHomeBinding
import com.olp.weco.model.PlantModel
import com.olp.weco.ui.common.activity.ScanActivity
import com.olp.weco.ui.dataloger.AddDataLoggerActivity
import com.olp.weco.ui.home.pv.PvStatusFragment
import com.olp.weco.ui.home.storage.HomeStatusFragment
import com.olp.weco.ui.station.viewmodel.StationViewModel
import com.olp.weco.view.pop.ListPopuwindow
import com.olp.weco.view.popuwindow.ListPopModel
import com.olp.lib.util.ActivityBridge
import com.olp.lib.util.gone
import com.olp.lib.util.visible

class HomeFragment : BaseFragment(), OnClickListener {


    private lateinit var _binding: FragmentHomeBinding


    private val viewModel: StationViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        initData()
        setliseners()
        return _binding.root
    }

    private fun setliseners() {
        _binding.header.tvTitle.setOnClickListener(this)
        _binding.srlRefresh.setOnRefreshListener {
            fetchPlantList()
        }

        _binding.header.ivAdd.setOnClickListener(this)

    }


    private fun initData() {
        viewModel.getPlantListLiveData.observe(viewLifecycleOwner) {
            _binding.srlRefresh.finishRefresh()
            if (it.first) {
                val second = it.second
                if (second == null || second.isEmpty()) {//没有电站  显示空
                    _binding.header.tvTitle.text = ""
                    _binding.srlRefresh.visible()
                    _binding.fragmentSystem.gone()
                } else {
                    _binding.srlRefresh.gone()
                    _binding.fragmentSystem.visible()
                    //默认选中第一个电站
                    _binding.header.tvTitle.text = second[0].plantName
                    //显示系统图
                    showSystemSatus(second[0])
                }
            }
        }


        fetchPlantList()
    }


    fun showSystemSatus(station: PlantModel) {
        viewModel.currentStation = station
        val stationType = station.plantType
        _binding.header.tvTitle.text = station.plantName
        //根据电站类型显示不同界面
        childFragmentManager.commit(true) {
            val findFragment = _binding.fragmentSystem.findFragment<Fragment>()
            if (PlantModel.PLANT_PV.equals(stationType,ignoreCase = true)) {//纯光伏电站
                if (findFragment is PvStatusFragment) {
                    findFragment.getDataByStationId(station.id.toString())
                } else {
                    val pvStatusFragment = PvStatusFragment()
                    //传参过去
                    val bundle = Bundle()
                    bundle.putString("stationId", station.id)
                    pvStatusFragment.arguments = bundle
                    replace(R.id.fragment_system, pvStatusFragment)
                }

            } else {
                if (findFragment is HomeStatusFragment) {
                    findFragment.getDataByStationId(station.id.toString())
                } else {
                    val homeStatusFragment = HomeStatusFragment()
                    //传参过去
                    val bundle = Bundle()
                    bundle.putString("stationId", station.id)
                    homeStatusFragment.arguments = bundle
                    replace(R.id.fragment_system, homeStatusFragment)
                }
            }
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

            p0===_binding.header.ivAdd->{
                    RequestPermissionHub.requestPermission(
                        childFragmentManager ,
                        arrayOf(Manifest.permission.CAMERA)
                    ) {
                        if (it) {
                            scan()
                        }
                    }
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
                options.add(ListPopModel(plant.plantName.toString(), false))
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
                    curItem?:""
                ) {
                    showSystemSatus(second[it])
                }
            }
        }
    }

    private fun scan() {
        ActivityBridge.startActivity(
            requireActivity(),
            ScanActivity.getIntent(context),
            object : ActivityBridge.OnActivityForResult {
                override fun onActivityForResult(
                    context: Context?,
                    resultCode: Int,
                    data: Intent?
                ) {
                    if (resultCode == RESULT_OK && data?.hasExtra(ScanActivity.KEY_CODE_TEXT) == true) {
                        val dataLoggerSN = data.getStringExtra(ScanActivity.KEY_CODE_TEXT)
                        //跳转到手动输入那里
                        AddDataLoggerActivity.start(context,viewModel.currentStation?.id,dataLoggerSN)
                    }
                }
            })
    }

}
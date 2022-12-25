package com.olp.weco.ui.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.commit
import com.olp.lib.util.ActivityBridge
import com.olp.lib.util.gone
import com.olp.lib.util.visible
import com.olp.weco.R
import com.olp.weco.base.BaseActivity
import com.olp.weco.databinding.ActivityHomeBinding
import com.olp.weco.model.PlantModel
import com.olp.weco.ui.common.activity.ScanActivity
import com.olp.weco.ui.common.fragment.RequestPermissionHub
import com.olp.weco.ui.dataloger.AddDataLoggerActivity
import com.olp.weco.ui.home.storage.HomeStatusFragment
import com.olp.weco.ui.station.viewmodel.StationViewModel
import com.olp.weco.view.pop.ListPopuwindow
import com.olp.weco.view.popuwindow.ListPopModel


class HomeActivity : BaseActivity() , View.OnClickListener {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, HomeActivity::class.java)
            context.startActivity(intent)
        }
    }


    private lateinit var _binding: ActivityHomeBinding

    private val viewModel: StationViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        initViews()
        initData()
        setliseners()

    }

    private fun initViews() {
        //绑定视图
        supportFragmentManager.commit(true) {
            replace(R.id.fragment_system, HomeStatusFragment())
        }
    }


    private fun initData() {
        viewModel.getPlantListLiveData.observe(this) {
            dismissDialog()
                val second = it.second
                if (second == null || second.isEmpty()) {//没有电站  显示空
                    _binding.header.tvTitle.gone()
                    //系统图显示为空
                    showSystemEmpty()
                    showOtherEmpty()

                } else {
                    _binding.header.tvTitle.visible()
                    //默认选中第一个电站
                    _binding.header.tvTitle.text = second[0].plantName
                    //显示系统图
                    showSystem()
                    //请求数据
                    showSystemSatus(second[0])
                }
        }

        fetchPlantList()
    }



    fun showSystemSatus(station: PlantModel) {
        viewModel.currentStation = station
        _binding.header.tvTitle.text = station.plantName

        val homeStatusFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_system) as HomeStatusFragment

        homeStatusFragment.getDataByStationId(station.id.toString())

    }



    fun showSystemEmpty(){
        val homeStatusFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_system) as HomeStatusFragment
        homeStatusFragment.showEmpty()
    }


    fun showSystem(){
        val homeStatusFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_system) as HomeStatusFragment
        homeStatusFragment.showSys()
    }





    private fun showOtherEmpty(){
        val dataFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_overview) as HomeStatusFragment
        dataFragment.showEmpty()
    }







    private fun fetchPlantList() {
        showDialog()
        viewModel.getPlantList()
    }


    private fun setliseners() {
        _binding.header.tvTitle.setOnClickListener(this)
        _binding.header.ivAdd.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when {
            v === _binding.header.tvTitle -> {
                showPlantList()
            }

            v===_binding.header.ivAdd->{
                RequestPermissionHub.requestPermission(
                    supportFragmentManager ,
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
            ListPopuwindow.showPop(
                this,
                options,
                _binding.header.tvTitle,
                curItem?:""
            ) {
                showSystemSatus(second[it])
            }
        }
    }



    private fun scan() {
        ActivityBridge.startActivity(
            this,
            ScanActivity.getIntent(this),
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
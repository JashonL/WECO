package com.olp.weco.ui.station.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import com.olp.weco.base.BaseActivity
import com.olp.weco.databinding.ActivityPlantListBinding
import com.olp.weco.ui.home.viewmodel.PlantFilterViewModel
import com.olp.weco.ui.station.viewmodel.PlantFilterModel

class PlantListActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, PlantListActivity::class.java)
            context.startActivity(intent)
        }
    }


    private lateinit var _binding: ActivityPlantListBinding

    private val filterViewModel: PlantFilterViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlantListBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        initLisenter()
        initData()
    }


    private fun initLisenter() {
        _binding.title.setOnLeftIconClickListener { finish() }
        _binding.btnAdd.setOnClickListener {
            AddTtchPlantActivity.start(this)
        }

    }



    private fun initData() {
        filterViewModel.setFilterType(PlantFilterModel.getDefaultFilter().filterType)
    }

}
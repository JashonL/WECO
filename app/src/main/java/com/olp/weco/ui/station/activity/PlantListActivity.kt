package com.olp.weco.ui.station.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.olp.weco.base.BaseActivity
import com.olp.weco.databinding.ActivityPlantListBinding

class PlantListActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, PlantListActivity::class.java)
            context.startActivity(intent)
        }
    }


    private lateinit var _binding: ActivityPlantListBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlantListBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        initLisenter()
    }


    private fun initLisenter() {
        _binding.title.setOnLeftIconClickListener { finish() }
        _binding.btnAdd.setOnClickListener {
            AddTtchPlantActivity.start(this)
        }

    }


}
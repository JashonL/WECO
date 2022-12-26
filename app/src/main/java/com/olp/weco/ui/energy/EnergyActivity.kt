package com.olp.weco.ui.energy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.olp.weco.base.BaseActivity
import com.olp.weco.databinding.ActivityEnergyBinding

class EnergyActivity:BaseActivity() {


    companion object{
        fun start(context: Context) {
            val intent = Intent(context, EnergyActivity::class.java)
            context.startActivity(intent)
        }
    }


    private lateinit var _binding: ActivityEnergyBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEnergyBinding.inflate(layoutInflater)
        setContentView(_binding.root)
    }



}
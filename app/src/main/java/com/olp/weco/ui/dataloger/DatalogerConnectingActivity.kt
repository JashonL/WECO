package com.olp.weco.ui.dataloger

import android.os.Bundle
import android.os.PersistableBundle
import com.olp.weco.base.BaseActivity
import com.olp.weco.databinding.ActivityDatalogerConnectingBinding

class DatalogerConnectingActivity : BaseActivity() {

    private lateinit var binding: ActivityDatalogerConnectingBinding


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityDatalogerConnectingBinding.inflate(layoutInflater)
    }
}
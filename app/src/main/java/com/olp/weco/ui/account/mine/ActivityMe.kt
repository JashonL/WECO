package com.olp.weco.ui.account.mine

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.olp.weco.base.BaseActivity
import com.olp.weco.databinding.ActivityMeBinding
import com.olp.weco.databinding.ActivitySettingBinding

class ActivityMe:BaseActivity() {

    companion object {

        fun start(context: Context?) {
            context?.startActivity(Intent(context, ActivityMe::class.java))
        }

    }



    private lateinit var binding: ActivityMeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}
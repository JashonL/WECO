package com.olp.weco.ui.device.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.olp.weco.base.BaseActivity

class HvBatBoxActivity:BaseActivity() {


    companion object {
        fun start(context: Context, plantId: String) {
            val intent = Intent(context, HvBatBoxActivity::class.java)
            context.startActivity(intent)
        }
    }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }


}
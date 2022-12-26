package com.olp.weco.ui.energy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.fragment.app.commit
import com.olp.weco.R
import com.olp.weco.base.BaseActivity
import com.olp.weco.databinding.ActivityEnergyBinding
import com.olp.weco.model.GeneralItemModel
import com.olp.weco.ui.energy.chart.EnergyChartFragment
import com.olp.weco.view.dialog.PickerDialog

class EnergyActivity : BaseActivity() ,OnClickListener{


    companion object {
        fun start(context: Context, plantId: String) {
            val intent = Intent(context, EnergyActivity::class.java)
            intent.putExtra("plantId", plantId)
            context.startActivity(intent)
        }
    }


    private lateinit var _binding: ActivityEnergyBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEnergyBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        initViews()

    }

    private fun initViews() {
        _binding.title.setOnLeftIconClickListener {
            finish()
        }
        _binding.title.setTitleText(getString(R.string.m17_energy))



        //绑定图表视图
        supportFragmentManager.commit {
            add(R.id.chart_container, EnergyChartFragment())
        }


    }





    override fun onClick(v: View?) {
        when{
            v===_binding.date.tvDateType->{

            }

        }
    }




    private fun selectDate(timeZones: Array<GeneralItemModel>) {
        PickerDialog.show(supportFragmentManager, timeZones) {

        }
    }


}
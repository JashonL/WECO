package com.olp.weco.view

import android.content.Context
import android.graphics.drawable.AdaptiveIconDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CompoundButton
import android.widget.CompoundButton.OnCheckedChangeListener
import android.widget.LinearLayout
import com.olp.weco.R
import com.olp.weco.databinding.ChartLengendBinding
import com.olp.weco.databinding.FragmentImpactBinding

class ChartLegend @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs), OnCheckedChangeListener {


    private var binding: ChartLengendBinding

    private var lisener: OnCheckLisener? = null


    init {
        val view = LayoutInflater.from(context).inflate(R.layout.chart_lengend, this)
        binding = ChartLengendBinding.bind(view)

        initViews()

    }

    private fun initViews() {
        binding.cbLedgend.setOnCheckedChangeListener(this)

    }


    fun setLabel(lable: String) {
        binding.tvLedgendTitle.text = lable
    }


    fun setCbstyle(drawable: Int) {
        binding.cbLedgend.setButtonDrawable(drawable)
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {

        if (lisener != null) {
            lisener?.oncheck(p1)
        }

    }


    interface OnCheckLisener {
        fun oncheck(p1: Boolean)
    }


}
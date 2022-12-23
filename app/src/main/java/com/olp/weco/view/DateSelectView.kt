package com.olp.weco.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.olp.lib.util.DateUtils
import com.olp.lib.util.Util
import com.olp.lib.util.invisible
import com.olp.lib.util.visible
import com.olp.weco.R
import com.olp.weco.databinding.DateSelectBinding
import com.olp.weco.ui.common.model.DataType
import com.olp.lib.view.dialog.DatePickerFragment
import com.olp.lib.view.dialog.OnDateSetListener
import java.util.*

class DateSelectView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private val binding: DateSelectBinding

    lateinit var nowDate: Date

    private var dateFormat = "yyyy-MM-dd"


    private var selectedListener: OntimeselectedListener? = null


    private var dateType = DataType.DAY


    init {
        val view = LayoutInflater.from(context).inflate(R.layout.date_select, this)
        binding = DateSelectBinding.bind(view)
        initData()
        initViews()
    }

    private fun initData() {
        nowDate = Date()
    }


    private fun initViews() {
        binding.ivNext.setOnClickListener(this)
        binding.ivPre.setOnClickListener(this)
        binding.tvDate.setOnClickListener(this)

        parserDate()
    }

    private fun parserDate() {
        when (dateType) {
            DataType.TOTAL -> this.invisible()
            DataType.YEAR -> {
                this.visible()
                binding.tvDate.text = DateUtils.yyyy_format(nowDate)
            }
            DataType.MONTH -> {
                this.visible()
                binding.tvDate.text = DateUtils.yyyy_MM_format(nowDate)
            }
            DataType.DAY -> {
                this.visible()
                binding.tvDate.text = DateUtils.yyyy_MM_dd_format(nowDate)
            }
        }

    }


    fun setDateType(@DataType type: Int) {
        dateType = type
        parserDate()
    }


    override fun onClick(v: View?) {
        when {
            v === binding.ivNext -> {
                nowDate = DateUtils.addDateDays(nowDate, 1)
                parserDate()
            }


            v === binding.ivPre -> {
                nowDate = DateUtils.addDateDays(nowDate, -1)
                parserDate()
            }

            v === binding.tvDate -> {
                showDatePickDialog()
            }

        }
        selectedListener?.onDateSelectedListener(nowDate)


    }


    private fun showDatePickDialog() {
        Util.getFragmentManagerForContext(context)?.also {
            DatePickerFragment.show(it, Date().time, object : OnDateSetListener {
                override fun onDateSet(date: Date) {
                    nowDate = date
                    parserDate()
                }
            })
        }
    }


    fun setListener(selectedListener: OntimeselectedListener) {
        this.selectedListener = selectedListener
    }


    interface OntimeselectedListener {
        fun onDateSelectedListener(date: Date)
    }


}
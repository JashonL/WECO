package com.olp.weco.ui.common.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.olp.weco.R
import com.olp.weco.base.BaseActivity
import com.olp.weco.base.BaseViewHolder
import com.olp.weco.base.OnItemClickListener
import com.olp.weco.databinding.ActivityCountryBinding
import com.olp.weco.databinding.CountryViewHolderBinding
import com.olp.weco.ui.common.viewmodel.SelectAreaViewModel
import com.olp.weco.view.itemdecoration.DividerItemDecoration
import com.olp.lib.util.ToastUtil

class CountryActivity : BaseActivity() {


    companion object {

        const val KEY_AREA = "key_area"

        fun start(context: Context?) {
            context?.startActivity(getIntent(context))
        }

        fun getIntent(context: Context?): Intent {
            return Intent(context, CountryActivity::class.java)
        }
    }

    private lateinit var binding: ActivityCountryBinding
    private val viewModel: SelectAreaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
        setListener()
    }


    private fun initView() {
        binding.rvList.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL,
                resources.getColor(R.color.color_line)
            )
        )
        binding.rvList.adapter = Adapter()





    }

    private fun setListener() {
        binding.etSearch.addTextChangedListener {
            val adapter = binding.rvList.adapter as Adapter
            val countryList = viewModel.areaListLiveData.value?.first
            val newList = mutableListOf<String>()

            if (countryList!=null) {
                for (s in countryList) {
                    if (s.contains(it.toString())) {
                        newList.add(s)
                    }
                }

            }


            adapter.refresh(newList)

        }
    }

    private fun initData() {
        viewModel.areaListLiveData.observe(this) {
            dismissDialog()
            if (it.second == null) {
                (binding.rvList.adapter as Adapter).refresh(it.first.toMutableList())
            } else {
                ToastUtil.show(it.second)
            }
        }
        showDialog()
        viewModel.fetchAreaList()
    }

    inner class Adapter(var countryList: MutableList<String> = mutableListOf()) :
        RecyclerView.Adapter<CountryViewHolder>(), OnItemClickListener {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
            return CountryViewHolder.create(parent, this)
        }

        override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
            holder.bindData(countryList[position])
        }

        override fun getItemCount(): Int {
            return countryList.size
        }

        @SuppressLint("NotifyDataSetChanged")
        fun refresh(countryList: MutableList<String>) {
            this.countryList.clear()
            this.countryList.addAll(countryList)
            notifyDataSetChanged()
        }

        override fun onItemClick(v: View?, position: Int) {
            val intent = Intent()
            if (v?.tag is String) {
                intent.putExtra(KEY_AREA, v.tag as String)
            }
            setResult(RESULT_OK, intent)
            finish()
        }

    }

    class CountryViewHolder(
        itemView: View,
        onItemClickListener: OnItemClickListener? = null
    ) :
        BaseViewHolder(itemView, onItemClickListener) {
        lateinit var binding: CountryViewHolderBinding

        companion object {
            fun create(
                parent: ViewGroup,
                onItemClickListener: OnItemClickListener? = null
            ): CountryViewHolder {
                val binding =
                    CountryViewHolderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                val holder = CountryViewHolder(binding.root, onItemClickListener)
                holder.binding = binding
                holder.binding.root.setOnClickListener(holder)
                return holder
            }
        }

        fun bindData(area: String?) {
            binding.tvArea.text = area
            binding.root.tag = area
        }

    }

}
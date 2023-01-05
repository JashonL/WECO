package com.olp.weco.ui.device.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.olp.weco.base.BaseActivity
import com.olp.weco.databinding.ActivityHvBatBoxBinding
import com.olp.weco.ui.device.fragment.DataFragmentOne
import com.olp.weco.ui.device.fragment.DataFragmentTwo
import com.olp.weco.ui.device.viewmodel.HvBatBoxViewModel

class HvBatBoxActivity : BaseActivity() {


    companion object {
        fun start(context: Context, deviceSN: String) {
            val intent = Intent(context, HvBatBoxActivity::class.java)
            intent.putExtra("deviceSN", deviceSN)
            context.startActivity(intent)
        }
    }


    private lateinit var binding: ActivityHvBatBoxBinding

    private val viewModel: HvBatBoxViewModel by viewModels()


    private val fragments: MutableList<Fragment> by lazy(LazyThreadSafetyMode.NONE) {
        mutableListOf(
            DataFragmentOne(),
            DataFragmentTwo(),
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHvBatBoxBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        initData()
    }


    private fun initViews() {
        //绑定Viewpager
        binding.viewpager.adapter = Adapter(this, fragments)
    }


    class Adapter(fragmenActivity: FragmentActivity, private val fragments: MutableList<Fragment>) :
        FragmentStateAdapter(fragmenActivity) {


        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }


    }


    private fun initData() {
        viewModel.deviceSn = intent.getStringExtra("deviceSN") ?: ""
        //请求数据
        showDialog()
        viewModel.getdeviceDetails()
    }


}
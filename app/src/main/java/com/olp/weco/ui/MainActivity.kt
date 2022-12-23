package com.olp.weco.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.olp.weco.R
import com.olp.weco.databinding.ActivityMainBinding
import com.olp.weco.model.PlantModel
import com.olp.weco.ui.array.ArrayFragment
import com.olp.weco.ui.energy.EnergyFragment
import com.olp.weco.ui.home.HomeFragment
import com.olp.weco.ui.manu.ManuFragment
import com.olp.weco.ui.station.fragment.StationFragment

class MainActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityMainBinding
    private val fragments by lazy(LazyThreadSafetyMode.NONE) {
        mutableListOf(
            HomeFragment(),
            EnergyFragment(),
            ArrayFragment(),
            StationFragment(),
            ManuFragment()
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //初始化Viewpager2
        val adapter = Adapter(this)
        val mainViewpager = binding.mainViewpager
        mainViewpager.adapter = adapter
        mainViewpager.offscreenPageLimit = mainViewpager.childCount
        adapter.refresh(fragments)
        mainViewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.navView.menu.getItem(position).isChecked = true
            }
        })




        binding.navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> mainViewpager.setCurrentItem(0, false)
                R.id.navigation_eneger -> mainViewpager.setCurrentItem(1, false)
                R.id.navigation_array -> mainViewpager.setCurrentItem(2, false)
                R.id.navigation_station -> mainViewpager.setCurrentItem(3, false)
                R.id.navigation_menu -> mainViewpager.setCurrentItem(4, false)
            }
            false
        }


    }


    inner class Adapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {

        private val fragments = mutableListOf<Fragment>()

        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }

        @SuppressLint("NotifyDataSetChanged")
        fun refresh(fragments: MutableList<Fragment>) {
            this.fragments.clear()
            this.fragments.addAll(fragments)
            notifyDataSetChanged()
        }


    }


    fun showHomeFragment(station: PlantModel) {
        val mainViewpager = binding.mainViewpager
        mainViewpager.setCurrentItem(0, false)
        (fragments[0] as HomeFragment).showSystemSatus(station)
    }


}
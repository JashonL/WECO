package com.olp.weco.ui.device.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.olp.weco.R
import com.olp.weco.base.BaseFragment
import com.olp.weco.base.BaseViewHolder
import com.olp.weco.base.OnItemClickListener
import com.olp.weco.databinding.FragmentDataTwoBinding
import com.olp.weco.databinding.ItemDeviceSocParam2Binding
import com.olp.weco.ui.device.model.DeviceDataModel
import com.olp.weco.ui.device.model.DeviceSocDataModel
import com.olp.weco.ui.device.viewmodel.HvBatBoxViewModel
import com.olp.weco.view.OnTabSelectedListener
import com.olp.weco.view.Tab
import com.olp.weco.view.TextTab
import com.olp.weco.view.itemdecoration.DividerItemDecoration
import kotlin.math.roundToInt

class DataFragmentTwo : BaseFragment() {


    private lateinit var binding: FragmentDataTwoBinding


    private val socDatalist: MutableList<MutableList<DeviceSocDataModel>> = mutableListOf()


    private val viewModel: HvBatBoxViewModel by activityViewModels()

    private lateinit var dataTitles: List<String>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDataTwoBinding.inflate(layoutInflater)
        initTitles()
        initLiseners()
        initViews()
        initData()
        return binding.root
    }

    private fun initTitles() {
        dataTitles = listOf(
            getString(R.string.m179_main_relay_staues), getString(R.string.m180_charged_voltage),
            getString(R.string.m181_battery_cycles), getString(R.string.m182_max_voltage),
            getString(R.string.m183_min_voltage), getString(R.string.m184_actual_current)
        )
    }


    private fun initViews() {
        //默认选中第一个
//        binding.tabLayout.setSelectTabPosition(0)

        binding.rlvData.layoutManager = GridLayoutManager(context, 2)
        binding.rlvData.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL,
                resources.getColor(R.color.nocolor),
                10f
            )
        )
        binding.rlvData.adapter = DataFragmentOne.Adapter(mutableListOf())


        binding.rlvData2.layoutManager = LinearLayoutManager(context)
        binding.rlvData2.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL,
                resources.getColor(R.color.nocolor),
                10f
            )
        )
        binding.rlvData2.adapter = SocAdapter(mutableListOf())

    }


    private fun initLiseners() {
        binding.tabLayout.addTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelect(selectTab: Tab, selectPosition: Int) {

            }

            override fun onTabSelect(selectTab: TextTab, selectPosition: Int) {
                if (socDatalist.size > selectPosition) {
                    val deviceSocDataModels = socDatalist[selectPosition]
                    (binding.rlvData2.adapter as SocAdapter).refresh(deviceSocDataModels)
                }
            }
        })

    }


    private fun initData() {
        viewModel.hvBatBoxLiveData.observe(viewLifecycleOwner) {
            dismissDialog()
            if (it.first) {
                //第一组数据
                val datas = mutableListOf<DeviceDataModel>()
                val second = it.second
                for (i in dataTitles.indices) {
                    val title = dataTitles[i]
                    val value = when (i) {
                        0 -> second?.mainRelayStaues
                        1 -> second?.chargedVoltage
                        2 -> second?.batteryCycles
                        3 -> second?.maxVoltage
                        4 -> second?.minVoltage
                        5 -> second?.actualCurrent
                        else -> {
                            ""
                        }
                    }
                    val model = DeviceDataModel(title, value.toString())
                    datas.add(model)
                }
                (binding.rlvData.adapter as DataFragmentOne.Adapter).refresh(datas)

                //第二组数据
                socDatalist.clear()
                for (i in 0..3) {
                    when (i) {
                        0 -> {
                            val datas2 = mutableListOf<DeviceSocDataModel>()
                            for (soc in 0..3) {
                                val title = "sub" + (soc + 1)
                                var bmsSoftWareVer = "0"
                                var bmsSoc = "0"
                                var bmsMaxSingleStringVol = "0"
                                var bmsMinSingleStringVol = "0"
                                var bmsMaxSingleStringTemp = "0"
                                var bmsMinSingleStringTemp = "0"

                                when (soc) {
                                    0 -> {
                                        bmsSoftWareVer = second?.bms1SoftWareVer ?: "0"
                                        bmsSoc = second?.bms1Soc ?: "0"
                                        bmsMaxSingleStringVol =
                                            second?.bms1MaxSingleStringVol + "V"
                                        bmsMinSingleStringVol =
                                            second?.bms1MinSingleStringVol + "V"
                                        bmsMaxSingleStringTemp =
                                            second?.bms1MaxSingleStringTemp + "℃"
                                        bmsMinSingleStringTemp =
                                            second?.bms1MinSingleStringTemp + "℃"
                                    }

                                    1 -> {
                                        bmsSoftWareVer = second?.bms2SoftWareVer ?: "0"
                                        bmsSoc = second?.bms2Soc ?: "0"
                                        bmsMaxSingleStringVol =
                                            second?.bms2MaxSingleStringVol + "V"
                                        bmsMinSingleStringVol =
                                            second?.bms2MinSingleStringVol + "V"
                                        bmsMaxSingleStringTemp =
                                            second?.bms2MaxSingleStringTemp + "℃"
                                        bmsMinSingleStringTemp =
                                            second?.bms2MinSingleStringTemp + "℃"
                                    }


                                    2 -> {
                                        bmsSoftWareVer = second?.bms3SoftWareVer ?: "0"
                                        bmsSoc = second?.bms3Soc ?: "0"
                                        bmsMaxSingleStringVol =
                                            second?.bms3MaxSingleStringVol + "V"
                                        bmsMinSingleStringVol =
                                            second?.bms3MinSingleStringVol + "V"
                                        bmsMaxSingleStringTemp =
                                            second?.bms3MaxSingleStringTemp + "℃"
                                        bmsMinSingleStringTemp =
                                            second?.bms3MinSingleStringTemp + "℃"
                                    }


                                    3 -> {
                                        bmsSoftWareVer = second?.bms4SoftWareVer ?: "0"
                                        bmsSoc = second?.bms4Soc ?: "0"
                                        bmsMaxSingleStringVol =
                                            second?.bms4MaxSingleStringVol + "V"
                                        bmsMinSingleStringVol =
                                            second?.bms4MinSingleStringVol + "V"
                                        bmsMaxSingleStringTemp =
                                            second?.bms4MaxSingleStringTemp + "℃"
                                        bmsMinSingleStringTemp =
                                            second?.bms4MinSingleStringTemp + "℃"
                                    }


                                }


                                val socData = DeviceSocDataModel(
                                    title,
                                    bmsSoftWareVer,
                                    bmsSoc.toDouble(),
                                    bmsMaxSingleStringVol,
                                    bmsMinSingleStringVol,
                                    bmsMaxSingleStringTemp,
                                    bmsMinSingleStringTemp
                                )
                                datas2.add(socData)
                            }
                            socDatalist.add(datas2)
                        }
                        1 -> {
                            val datas2 = mutableListOf<DeviceSocDataModel>()
                            for (soc in 0..3) {
                                val title = "sub" + (soc + 1)
                                var bmsSoftWareVer = "0"
                                var bmsSoc = "0"
                                var bmsMaxSingleStringVol = "0"
                                var bmsMinSingleStringVol = "0"
                                var bmsMaxSingleStringTemp = "0"
                                var bmsMinSingleStringTemp = "0"

                                when (soc) {
                                    0 -> {
                                        bmsSoftWareVer = second?.bms5SoftWareVer ?: "0"
                                        bmsSoc = second?.bms5Soc ?: "0"
                                        bmsMaxSingleStringVol =
                                            second?.bms5MaxSingleStringVol + "V"
                                        bmsMinSingleStringVol =
                                            second?.bms5MinSingleStringVol + "V"
                                        bmsMaxSingleStringTemp =
                                            second?.bms5MaxSingleStringTemp + "℃"
                                        bmsMinSingleStringTemp =
                                            second?.bms5MinSingleStringTemp + "℃"
                                    }

                                    1 -> {
                                        bmsSoftWareVer = second?.bms6SoftWareVer ?: "0"
                                        bmsSoc = second?.bms6Soc ?: "0"
                                        bmsMaxSingleStringVol =
                                            second?.bms6MaxSingleStringVol + "V"
                                        bmsMinSingleStringVol =
                                            second?.bms6MinSingleStringVol + "V"
                                        bmsMaxSingleStringTemp =
                                            second?.bms6MaxSingleStringTemp + "℃"
                                        bmsMinSingleStringTemp =
                                            second?.bms6MinSingleStringTemp + "℃"
                                    }


                                    2 -> {
                                        bmsSoftWareVer = second?.bms7SoftWareVer ?: "0"
                                        bmsSoc = second?.bms7Soc ?: "0"
                                        bmsMaxSingleStringVol =
                                            second?.bms7MaxSingleStringVol + "V"
                                        bmsMinSingleStringVol =
                                            second?.bms7MinSingleStringVol + "V"
                                        bmsMaxSingleStringTemp =
                                            second?.bms7MaxSingleStringTemp + "℃"
                                        bmsMinSingleStringTemp =
                                            second?.bms7MinSingleStringTemp + "℃"
                                    }


                                    3 -> {
                                        bmsSoftWareVer = second?.bms8SoftWareVer ?: "0"
                                        bmsSoc = second?.bms8Soc ?: "0"
                                        bmsMaxSingleStringVol =
                                            second?.bms8MaxSingleStringVol + "V"
                                        bmsMinSingleStringVol =
                                            second?.bms8MinSingleStringVol + "V"
                                        bmsMaxSingleStringTemp =
                                            second?.bms8MaxSingleStringTemp + "℃"
                                        bmsMinSingleStringTemp =
                                            second?.bms8MinSingleStringTemp + "℃"
                                    }


                                }


                                val socData = DeviceSocDataModel(
                                    title,
                                    bmsSoftWareVer,
                                    bmsSoc.toDouble(),
                                    bmsMaxSingleStringVol,
                                    bmsMinSingleStringVol,
                                    bmsMaxSingleStringTemp,
                                    bmsMinSingleStringTemp
                                )
                                datas2.add(socData)
                            }
                            socDatalist.add(datas2)
                        }
                        2 -> {
                            val datas2 = mutableListOf<DeviceSocDataModel>()
                            for (soc in 0..3) {
                                val title = "sub" + (soc + 1)
                                var bmsSoftWareVer = "0"
                                var bmsSoc = "0"
                                var bmsMaxSingleStringVol = "0"
                                var bmsMinSingleStringVol = "0"
                                var bmsMaxSingleStringTemp = "0"
                                var bmsMinSingleStringTemp = "0"

                                when (soc) {
                                    0 -> {
                                        bmsSoftWareVer = second?.bms9SoftWareVer ?: "0"
                                        bmsSoc = second?.bms9Soc ?: "0"
                                        bmsMaxSingleStringVol =
                                            second?.bms9MaxSingleStringVol + "V"
                                        bmsMinSingleStringVol =
                                            second?.bms9MinSingleStringVol + "V"
                                        bmsMaxSingleStringTemp =
                                            second?.bms9MaxSingleStringTemp + "℃"
                                        bmsMinSingleStringTemp =
                                            second?.bms9MinSingleStringTemp + "℃"
                                    }

                                    1 -> {
                                        bmsSoftWareVer = second?.bms10SoftWareVer ?: "0"
                                        bmsSoc = second?.bms10Soc ?: "0"
                                        bmsMaxSingleStringVol =
                                            second?.bms10MaxSingleStringVol + "V"
                                        bmsMinSingleStringVol =
                                            second?.bms10MinSingleStringVol + "V"
                                        bmsMaxSingleStringTemp =
                                            second?.bms10MaxSingleStringTemp + "℃"
                                        bmsMinSingleStringTemp =
                                            second?.bms10MinSingleStringTemp + "℃"
                                    }


                                    2 -> {
                                        bmsSoftWareVer = second?.bms11SoftWareVer ?: "0"
                                        bmsSoc = second?.bms11Soc ?: "0"
                                        bmsMaxSingleStringVol =
                                            second?.bms11MaxSingleStringVol + "V"
                                        bmsMinSingleStringVol =
                                            second?.bms11MinSingleStringVol + "V"
                                        bmsMaxSingleStringTemp =
                                            second?.bms11MaxSingleStringTemp + "℃"
                                        bmsMinSingleStringTemp =
                                            second?.bms11MinSingleStringTemp + "℃"
                                    }


                                    3 -> {
                                        bmsSoftWareVer = second?.bms12SoftWareVer ?: "0"
                                        bmsSoc = second?.bms12Soc ?: "0"
                                        bmsMaxSingleStringVol =
                                            second?.bms12MaxSingleStringVol + "V"
                                        bmsMinSingleStringVol =
                                            second?.bms12MinSingleStringVol + "V"
                                        bmsMaxSingleStringTemp =
                                            second?.bms12MaxSingleStringTemp + "℃"
                                        bmsMinSingleStringTemp =
                                            second?.bms12MinSingleStringTemp + "℃"
                                    }


                                }


                                val socData = DeviceSocDataModel(
                                    title,
                                    bmsSoftWareVer,
                                    bmsSoc.toDouble(),
                                    bmsMaxSingleStringVol,
                                    bmsMinSingleStringVol,
                                    bmsMaxSingleStringTemp,
                                    bmsMinSingleStringTemp
                                )
                                datas2.add(socData)
                            }
                            socDatalist.add(datas2)
                        }
                        3 -> {
                            val datas2 = mutableListOf<DeviceSocDataModel>()
                            for (soc in 0..3) {
                                val title = "sub" + (soc + 1)
                                var bmsSoftWareVer = "0"
                                var bmsSoc = "0"
                                var bmsMaxSingleStringVol = "0"
                                var bmsMinSingleStringVol = "0"
                                var bmsMaxSingleStringTemp = "0"
                                var bmsMinSingleStringTemp = "0"

                                when (soc) {
                                    0 -> {
                                        bmsSoftWareVer = second?.bms13SoftWareVer ?: "0"
                                        bmsSoc = second?.bms9Soc ?: "0"
                                        bmsMaxSingleStringVol =
                                            second?.bms13MaxSingleStringVol + "V"
                                        bmsMinSingleStringVol =
                                            second?.bms13MinSingleStringVol + "V"
                                        bmsMaxSingleStringTemp =
                                            second?.bms13MaxSingleStringTemp + "℃"
                                        bmsMinSingleStringTemp =
                                            second?.bms13MinSingleStringTemp + "℃"
                                    }

                                    1 -> {
                                        bmsSoftWareVer = second?.bms14SoftWareVer ?: "0"
                                        bmsSoc = second?.bms14Soc ?: "0"
                                        bmsMaxSingleStringVol =
                                            second?.bms14MaxSingleStringVol + "V"
                                        bmsMinSingleStringVol =
                                            second?.bms14MinSingleStringVol + "V"
                                        bmsMaxSingleStringTemp =
                                            second?.bms14MaxSingleStringTemp + "℃"
                                        bmsMinSingleStringTemp =
                                            second?.bms14MinSingleStringTemp + "℃"
                                    }


                                    2 -> {
                                        bmsSoftWareVer = second?.bms15SoftWareVer ?: "0"
                                        bmsSoc = second?.bms15Soc ?: "0"
                                        bmsMaxSingleStringVol =
                                            second?.bms15MaxSingleStringVol + "V"
                                        bmsMinSingleStringVol =
                                            second?.bms15MinSingleStringVol + "V"
                                        bmsMaxSingleStringTemp =
                                            second?.bms15MaxSingleStringTemp + "℃"
                                        bmsMinSingleStringTemp =
                                            second?.bms15MinSingleStringTemp + "℃"
                                    }


                                    3 -> {
                                        bmsSoftWareVer = second?.bms16SoftWareVer ?: "0"
                                        bmsSoc = second?.bms16Soc ?: "0"
                                        bmsMaxSingleStringVol =
                                            second?.bms16MaxSingleStringVol + "V"
                                        bmsMinSingleStringVol =
                                            second?.bms16MinSingleStringVol + "V"
                                        bmsMaxSingleStringTemp =
                                            second?.bms16MaxSingleStringTemp + "℃"
                                        bmsMinSingleStringTemp =
                                            second?.bms16MinSingleStringTemp + "℃"
                                    }


                                }


                                val socData = DeviceSocDataModel(
                                    title,
                                    bmsSoftWareVer,
                                    bmsSoc.toDouble(),
                                    bmsMaxSingleStringVol,
                                    bmsMinSingleStringVol,
                                    bmsMaxSingleStringTemp,
                                    bmsMinSingleStringTemp
                                )
                                datas2.add(socData)
                            }
                            socDatalist.add(datas2)
                        }
                    }
                }

                //刷新数据
                var selectTabPosition = binding.tabLayout.getSelectTabPosition()
                if (selectTabPosition == -1) {
                    selectTabPosition = 0
                }
                binding.tabLayout.setSelectTabPosition(selectTabPosition, true)


            }


        }

    }


    class SocAdapter(val datalist: MutableList<DeviceSocDataModel>) :
        RecyclerView.Adapter<BaseViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
            return DataTwoViewHolder.create(parent)
        }

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            (holder as DataTwoViewHolder).bindData(datalist[position])
        }

        override fun getItemCount(): Int {
            return datalist.size
        }

        @SuppressLint("NotifyDataSetChanged")
        fun refresh(deviceList: MutableList<DeviceSocDataModel>?) {
            this.datalist.clear()
            if (!deviceList.isNullOrEmpty()) {
                this.datalist.addAll(deviceList)
            }
            notifyDataSetChanged()
        }


    }


    class DataTwoViewHolder(
        itemView: View,
        onItemClickListener: OnItemClickListener? = null
    ) :
        BaseViewHolder(itemView, onItemClickListener) {
        lateinit var binding: ItemDeviceSocParam2Binding

        companion object {
            fun create(
                parent: ViewGroup,
                onItemClickListener: OnItemClickListener? = null
            ): DataTwoViewHolder {
                val binding =
                    ItemDeviceSocParam2Binding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                val holder = DataTwoViewHolder(binding.root, onItemClickListener)
                holder.binding = binding
                holder.binding.root.setOnClickListener(holder)
                return holder
            }
        }

        fun bindData(dataModel: DeviceSocDataModel?) {
            binding.tvTitle.text = dataModel?.title
            binding.tvSoc.text = dataModel?.soc

            binding.tvMaxVol.text = dataModel?.MaxVoltage
            binding.tvMinVol.text = dataModel?.MinVoltage

            binding.tvMaxTemp.text = dataModel?.Maxtemperature
            binding.tvMinTemp.text = dataModel?.Mintemperature


            val percenter = dataModel?.percent!!.roundToInt()

            binding.tvProgress.text = "$percenter%"

            binding.pbProgress.progress = percenter
        }

    }


}
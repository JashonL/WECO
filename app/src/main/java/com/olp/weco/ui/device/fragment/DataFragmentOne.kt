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
import com.olp.weco.databinding.FragmentDataOneBinding
import com.olp.weco.databinding.ItemDeviceParam1Binding
import com.olp.weco.ui.device.model.DeviceDataModel
import com.olp.weco.ui.device.viewmodel.HvBatBoxViewModel
import com.olp.weco.view.itemdecoration.DividerItemDecoration

class DataFragmentOne : BaseFragment() {

    private lateinit var bingding: FragmentDataOneBinding


    private val viewModel: HvBatBoxViewModel by activityViewModels()


    private lateinit var dataTitles: List<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bingding = FragmentDataOneBinding.inflate(inflater)
        initTitles()
        initViews()
        initData()
        return bingding.root
    }

    private fun initTitles() {
        dataTitles = listOf(
            getString(R.string.m163_battery_model), getString(R.string.m164_battery_sn),
            getString(R.string.m165_battery_id), getString(R.string.m166_bms_type),
            getString(R.string.m167_fw_version_1), getString(R.string.m168_actual_protocol),
            getString(R.string.m169_fw_version_1), getString(R.string.m170_current),
            getString(R.string.m171_discharge_power), getString(R.string.m172_battery_soc),
            getString(R.string.m173_max_volt), getString(R.string.m174_min_volt),
            getString(R.string.m175_max_tem), getString(R.string.m176_min_tem),
            getString(R.string.m177_charge_energy), getString(R.string.m178_discharge_energy)
        )
    }


    private fun initData() {
        viewModel.hvBatBoxLiveData.observe(viewLifecycleOwner) {
            dismissDialog()
            if (it.first) {
                val second = it.second
                val datas = mutableListOf<DeviceDataModel>()
                for (i in dataTitles.indices) {
                    val title = dataTitles[i]
                    val value = when (i) {
                        0 -> second?.batteryMode
                        1 -> second?.batterySn
                        2 -> second?.batteryId
                        3 -> second?.bmsType
                        4 -> second?.fwVersion
                        5 -> second?.actualProtocol
                        6 -> second?.totalVoltage
                        7 -> second?.current
                        8 -> second?.dischargePower
                        9 -> second?.batterySoc
                        10 -> second?.maxVolt
                        11 -> second?.minVolt
                        12 -> second?.maxTem
                        13 -> second?.bms1MinSingleStringTemp
                        14 -> second?.chargeEnergy
                        15 -> second?.dischargeEnergy
                        else -> {
                            ""
                        }
                    }
                    val model = DeviceDataModel(title, value.toString())
                    datas.add(model)
                }

                (bingding.rlvData.adapter as Adapter).refresh(datas)

            }


        }

    }


    private fun initViews() {
        bingding.rlvData.layoutManager = GridLayoutManager(context, 2)
        bingding.rlvData.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL,
                resources.getColor(R.color.nocolor),
                10f
            )
        )
        bingding.rlvData.adapter = Adapter(mutableListOf())

    }


    class Adapter(val datalist: MutableList<DeviceDataModel>) :
        RecyclerView.Adapter<BaseViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
            return DataOneViewHolder.create(parent)
        }

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            (holder as DataOneViewHolder).bindData(datalist[position])
        }

        override fun getItemCount(): Int {
            return datalist.size
        }

        @SuppressLint("NotifyDataSetChanged")
        fun refresh(deviceList: MutableList<DeviceDataModel>?) {
            this.datalist.clear()
            if (!deviceList.isNullOrEmpty()) {
                this.datalist.addAll(deviceList)
            }
            notifyDataSetChanged()
        }


    }


    class DataOneViewHolder(
        itemView: View,
        onItemClickListener: OnItemClickListener? = null
    ) :
        BaseViewHolder(itemView, onItemClickListener) {
        lateinit var binding: ItemDeviceParam1Binding

        companion object {
            fun create(
                parent: ViewGroup,
                onItemClickListener: OnItemClickListener? = null
            ): DataOneViewHolder {
                val binding =
                    ItemDeviceParam1Binding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                val holder = DataOneViewHolder(binding.root, onItemClickListener)
                holder.binding = binding
                holder.binding.root.setOnClickListener(holder)
                return holder
            }
        }

        fun bindData(dataModel: DeviceDataModel?) {
            binding.tvKey.text = dataModel?.key
            binding.tvValue.text = dataModel?.value
        }

    }


}
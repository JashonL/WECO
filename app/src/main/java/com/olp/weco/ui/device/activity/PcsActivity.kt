package com.olp.weco.ui.device.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.olp.weco.R
import com.olp.weco.base.BaseActivity
import com.olp.weco.base.BaseViewHolder
import com.olp.weco.base.OnItemClickListener
import com.olp.weco.databinding.ActivityPcsBinding
import com.olp.weco.databinding.ItemDeviceParam1Binding
import com.olp.weco.databinding.ItemDeviceParamTitleBinding
import com.olp.weco.ui.device.fragment.DataFragmentOne
import com.olp.weco.ui.device.model.DeviceDataModel
import com.olp.weco.ui.device.model.PcsDataModel
import com.olp.weco.ui.device.viewmodel.PcsViewModel
import com.olp.weco.view.itemdecoration.DividerItemDecoration

class PcsActivity : BaseActivity() {


    companion object {
        fun start(context: Context, deviceSN: String) {
            val intent = Intent(context, PcsActivity::class.java)
            intent.putExtra("deviceSN", deviceSN)
            context.startActivity(intent)
        }
    }


    private val viewModel: PcsViewModel by viewModels()

    private lateinit var binding: ActivityPcsBinding


    private val dataTitles = listOf(
        getString(R.string.m187_device_sn), getString(R.string.m188_rated_power),
        getString(R.string.m189_model), getString(R.string.m190_firmware_version),


        getString(R.string.m191_ac_dc_data),
        getString(R.string.m192_u_phase_voltage), getString(R.string.m193_u_phase_current),
        getString(R.string.m194_v_phase_voltage), getString(R.string.m195_v_phase_current),
        getString(R.string.m196_w_phase_voltage), getString(R.string.m197_w_phase_current),
        getString(R.string.m198_active_power), getString(R.string.m199_reactive_power),
        getString(R.string.m200_ac_frequency), getString(R.string.m201_operating_mode),
        getString(R.string.m202_device_status),


        getString(R.string.m203_dc_dc_data),
        getString(R.string.m204_high_voltage), getString(R.string.m205_inlet_air_temperature),
        getString(R.string.m206_low_side_voltage), getString(R.string.m207_low_side_current),
        getString(R.string.m208_low_side_power), getString(R.string.m209_dc_type),
        getString(R.string.m210_dc_operating_state), getString(R.string.m211_dc_alarm_status),
        getString(R.string.m212_dc_fault_status),


        getString(R.string.m213_bat_data),
        getString(R.string.m94_voltage), getString(R.string.m214_run_status),
        getString(R.string.m62_soc), getString(R.string.m170_current),
        getString(R.string.m215_max_charge_current), getString(R.string.m216_soh),
        getString(R.string.m217_max_cell_vol), getString(R.string.m218_max_discharge_current),
        getString(R.string.m219_max_cell_temp), getString(R.string.m220_min_cell_temp),
        getString(R.string.m221_forbid_charge), getString(R.string.m222_min_cell_voltage),
        getString(R.string.m223_forbid_discharge),


        )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPcsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        initData()
    }


    private fun initViews() {
        //初始化recyclerview
        binding.rlvData.layoutManager = LinearLayoutManager(this)
        binding.rlvData.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL,
                resources.getColor(R.color.nocolor),
                10f
            )
        )
        binding.rlvData.adapter = DataFragmentOne.Adapter(mutableListOf())

    }


    private fun initData() {

        viewModel.pcsLiveData.observe(this) {
            dismissDialog()
            if (it.first) {
                val second = it.second
                val datas = mutableListOf<DeviceDataModel>()
                for (i in dataTitles.indices) {
                    val title = dataTitles[i]
                    val value = when (i) {
                        0 -> second?.deviceSn
                        1 -> second?.ratedPower
                        2 -> second?.model
                        3 -> second?.firmwareVersion

                        4 -> getString(R.string.m191_ac_dc_data)
                        5 -> second?.uPhaseVoltage1
                        6 -> second?.uPhaseCurrent1
                        7 -> second?.vPhaseVoltage1
                        8 -> second?.vPhaseCurrent1
                        9 -> second?.wPhaseVoltage1
                        10 -> second?.wPhaseCurrent1
                        11 -> second?.activePower1
                        12 -> second?.reactivePower1
                        13 -> second?.acFrequency1
                        14 -> second?.operatingMode
                        15 -> second?.deviceStatus1


                        16 -> getString(R.string.m203_dc_dc_data)
                        17 -> second?.highSideVoltage1
                        18 -> second?.uPhaseCurrent1
                        19 -> second?.vPhaseVoltage1
                        20 -> second?.vPhaseCurrent1
                        21 -> second?.wPhaseVoltage1
                        22 -> second?.wPhaseCurrent1
                        23 -> second?.activePower1
                        24 -> second?.reactivePower1
                        25 -> second?.acFrequency1
                        26 -> second?.operatingMode
                        27 -> second?.deviceStatus1

                        else -> {
                            ""
                        }
                    }
                    val model = DeviceDataModel(title, value.toString())
                    datas.add(model)
                }


            }

        }


        //请求数据
        showDialog()
        viewModel.getdeviceDetails()
    }


    class Adapter(val datalist: MutableList<PcsDataModel>) :
        RecyclerView.Adapter<BaseViewHolder>(), OnItemClickListener {

        companion object {
            const val PCS_ITEM_DATA = 0
            const val PCS_ITEM_TITLE = 1
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
            return if (viewType == PCS_ITEM_TITLE) {
                PCSTitleViewHolder.create(parent, this)
            } else {
                PCSDataViewHolder.create(parent, this)

            }

        }

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            if (holder is PCSTitleViewHolder) {
                holder.bindData(datalist[position])
            } else if (holder is PCSDataViewHolder) {
                holder.bindData(datalist[position])
            }
        }

        override fun getItemCount(): Int {
            return datalist.size
        }


        override fun getItemViewType(position: Int): Int {
            val model = datalist.get(position)
            return if (model.isTitle) PCS_ITEM_TITLE else PCS_ITEM_DATA
        }


        @SuppressLint("NotifyDataSetChanged")
        fun refresh(deviceList: MutableList<PcsDataModel>?) {
            this.datalist.clear()
            if (!deviceList.isNullOrEmpty()) {
                this.datalist.addAll(deviceList)
            }
            notifyDataSetChanged()
        }


    }


    class PCSDataViewHolder(
        itemView: View,
        onItemClickListener: OnItemClickListener? = null
    ) :
        BaseViewHolder(itemView, onItemClickListener) {
        lateinit var binding: ItemDeviceParam1Binding

        companion object {
            fun create(
                parent: ViewGroup,
                onItemClickListener: OnItemClickListener? = null
            ): PCSDataViewHolder {
                val binding =
                    ItemDeviceParam1Binding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                val holder = PCSDataViewHolder(binding.root, onItemClickListener)
                holder.binding = binding
                holder.binding.root.setOnClickListener(holder)
                return holder
            }
        }

        fun bindData(dataModel: PcsDataModel?) {
            binding.tvKey.text = dataModel?.key
            binding.tvValue.text = dataModel?.value
        }

    }


    class PCSTitleViewHolder(
        itemView: View,
        onItemClickListener: OnItemClickListener? = null
    ) : BaseViewHolder(itemView, onItemClickListener) {
        lateinit var binding: ItemDeviceParamTitleBinding

        companion object {
            fun create(
                parent: ViewGroup,
                onItemClickListener: OnItemClickListener? = null
            ): PCSTitleViewHolder {
                val binding =
                    ItemDeviceParamTitleBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                val holder = PCSTitleViewHolder(binding.root, onItemClickListener)
                holder.binding = binding
                holder.binding.root.setOnClickListener(holder)
                return holder
            }
        }

        fun bindData(dataModel: PcsDataModel?) {
            binding.tvTitle.text = dataModel?.title
        }


    }


}
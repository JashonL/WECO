package com.olp.weco.ui.device.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.olp.weco.R
import com.olp.weco.base.BaseActivity
import com.olp.weco.base.BaseViewHolder
import com.olp.weco.base.OnItemClickListener
import com.olp.weco.databinding.ActivityPcsBinding
import com.olp.weco.databinding.ItemDeviceParam1Binding
import com.olp.weco.databinding.ItemDeviceParamTitleBinding
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


    private lateinit var dataTitles: List<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPcsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initTitles()
        initViews()
        initData()
    }

    //赶时间 先这样写 (后续优化：把每个设置项抽取成bean)
    private fun initTitles() {
        dataTitles = listOf(
            getString(R.string.m187_device_sn), getString(R.string.m188_rated_power),
            getString(R.string.m224_device_type), getString(R.string.m225_soft_ware_version),
            getString(R.string.m190_firmware_version),


            getString(R.string.m191_pcs_data_1),
            getString(R.string.m192_u_phase_voltage), getString(R.string.m193_u_phase_current),
            getString(R.string.m194_v_phase_voltage), getString(R.string.m195_v_phase_current),
            getString(R.string.m196_w_phase_voltage), getString(R.string.m197_w_phase_current),
            getString(R.string.m198_active_power), getString(R.string.m199_reactive_power),
            getString(R.string.m200_ac_frequency), getString(R.string.m226_work_mode),
            getString(R.string.m202_device_status),


            getString(R.string.m232_pcs_data_2),
            getString(R.string.m192_u_phase_voltage), getString(R.string.m193_u_phase_current),
            getString(R.string.m194_v_phase_voltage), getString(R.string.m195_v_phase_current),
            getString(R.string.m196_w_phase_voltage), getString(R.string.m197_w_phase_current),
            getString(R.string.m198_active_power), getString(R.string.m199_reactive_power),
            getString(R.string.m200_ac_frequency), getString(R.string.m226_work_mode),
            getString(R.string.m202_device_status),


            getString(R.string.m233_pcs_data_3),
            getString(R.string.m192_u_phase_voltage), getString(R.string.m193_u_phase_current),
            getString(R.string.m194_v_phase_voltage), getString(R.string.m195_v_phase_current),
            getString(R.string.m196_w_phase_voltage), getString(R.string.m197_w_phase_current),
            getString(R.string.m198_active_power), getString(R.string.m199_reactive_power),
            getString(R.string.m200_ac_frequency), getString(R.string.m226_work_mode),
            getString(R.string.m202_device_status),


            getString(R.string.m234_pcs_data_4),
            getString(R.string.m192_u_phase_voltage), getString(R.string.m193_u_phase_current),
            getString(R.string.m194_v_phase_voltage), getString(R.string.m195_v_phase_current),
            getString(R.string.m196_w_phase_voltage), getString(R.string.m197_w_phase_current),
            getString(R.string.m198_active_power), getString(R.string.m199_reactive_power),
            getString(R.string.m200_ac_frequency), getString(R.string.m226_work_mode),
            getString(R.string.m202_device_status),


            getString(R.string.m203_mppt_data_1),
            getString(R.string.m227_input_voltage), getString(R.string.m228_inlet_temp),
            getString(R.string.m227_input_power), getString(R.string.m228_input_current),
            getString(R.string.m227_dc_run_state), getString(R.string.m228_dc_access_type),
            getString(R.string.m229_dc_fault_status), getString(R.string.m230_high_side_voltage),


            getString(R.string.m235_mppt_data_2),
            getString(R.string.m227_input_voltage), getString(R.string.m228_inlet_temp),
            getString(R.string.m227_input_power), getString(R.string.m228_input_current),
            getString(R.string.m227_dc_run_state), getString(R.string.m228_dc_access_type),
            getString(R.string.m229_dc_fault_status), getString(R.string.m230_high_side_voltage),


            getString(R.string.m236_mppt_data_3),
            getString(R.string.m227_input_voltage), getString(R.string.m228_inlet_temp),
            getString(R.string.m227_input_power), getString(R.string.m228_input_current),
            getString(R.string.m227_dc_run_state), getString(R.string.m228_dc_access_type),
            getString(R.string.m229_dc_fault_status), getString(R.string.m230_high_side_voltage),

            getString(R.string.m237_mppt_data_4),
            getString(R.string.m227_input_voltage), getString(R.string.m228_inlet_temp),
            getString(R.string.m227_input_power), getString(R.string.m228_input_current),
            getString(R.string.m227_dc_run_state), getString(R.string.m228_dc_access_type),
            getString(R.string.m229_dc_fault_status), getString(R.string.m230_high_side_voltage),


            getString(R.string.m213_bat_data),
            getString(R.string.m94_voltage), getString(R.string.m214_run_status),
            getString(R.string.m62_soc), getString(R.string.m170_current),
            getString(R.string.m215_max_charge_current), getString(R.string.m216_soh),
            getString(R.string.m217_max_cell_vol), getString(R.string.m218_max_discharge_current),
            getString(R.string.m219_max_cell_temp), getString(R.string.m220_min_cell_temp),
            getString(R.string.m222_min_cell_voltage),


            )

    }


    private fun initViews() {
        //初始化recyclerview
        binding.rlvData.layoutManager = GridLayoutManager(this, 2)
        binding.rlvData.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL,
                resources.getColor(R.color.nocolor),
                10f
            )
        )

        setGridManager(binding.rlvData.layoutManager as GridLayoutManager)
        binding.rlvData.adapter = Adapter(mutableListOf())

    }


    private fun setGridManager(gridManager: GridLayoutManager) {
        gridManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == 5 || position == 17 || position == 29
                    || position == 41 || position == 53 || position == 62 ||
                    position == 71 || position == 80 || position == 89
                ) {
                    gridManager.spanCount
                } else 1


            }
        }
    }


    private fun initData() {

        viewModel.deviceSn = intent.getStringExtra("deviceSN").toString()

        viewModel.pcsLiveData.observe(this) {
            dismissDialog()
            if (it.first) {
                val second = it.second
                val datas = mutableListOf<PcsDataModel>()
                for (i in dataTitles.indices) {
                    val title = dataTitles[i]
                    val value = when (i) {
                        0 -> second?.deviceSn
                        1 -> second?.ratedPower
                        2 -> second?.deviceType
                        3 -> second?.softwareVersion
                        4 -> second?.firmwareVersion


                        5 -> getString(R.string.m191_pcs_data_1)
                        6 -> second?.uPhaseVoltage1
                        7 -> second?.uPhaseCurrent1
                        8 -> second?.vPhaseVoltage1
                        9 -> second?.vPhaseCurrent1
                        10 -> second?.wPhaseVoltage1
                        11 -> second?.wPhaseCurrent1
                        12 -> second?.activePower1
                        13 -> second?.reactivePower1
                        14 -> second?.acFrequency1
                        15 -> second?.operatingMode
                        16 -> second?.deviceStatus1


                        17 -> getString(R.string.m232_pcs_data_2)
                        18 -> second?.uPhaseVoltage2
                        19 -> second?.uPhaseCurrent2
                        20 -> second?.vPhaseVoltage2
                        21 -> second?.vPhaseCurrent2
                        22 -> second?.wPhaseVoltage2
                        23 -> second?.wPhaseCurrent2
                        24 -> second?.activePower2
                        25 -> second?.reactivePower2
                        26 -> second?.acFrequency2
                        27 -> second?.operatingMode
                        28 -> second?.deviceStatus2


                        29 -> getString(R.string.m233_pcs_data_3)
                        30 -> second?.uPhaseVoltage3
                        31 -> second?.uPhaseCurrent3
                        32 -> second?.vPhaseVoltage3
                        33 -> second?.vPhaseCurrent3
                        34 -> second?.wPhaseVoltage3
                        35 -> second?.wPhaseCurrent3
                        36 -> second?.activePower3
                        37 -> second?.reactivePower3
                        38 -> second?.acFrequency3
                        39 -> second?.operatingMode
                        40 -> second?.deviceStatus3


                        41 -> getString(R.string.m234_pcs_data_4)
                        42 -> second?.uPhaseVoltage4
                        43 -> second?.uPhaseCurrent4
                        44 -> second?.vPhaseVoltage4
                        45 -> second?.vPhaseCurrent4
                        46 -> second?.wPhaseVoltage4
                        47 -> second?.wPhaseCurrent4
                        48 -> second?.activePower4
                        49 -> second?.reactivePower4
                        50 -> second?.acFrequency4
                        51 -> second?.operatingMode
                        52 -> second?.deviceStatus4


                        53 -> getString(R.string.m203_mppt_data_1)
                        54 -> second?.inputVoltage1
                        55 -> second?.inputTemp1
                        56 -> second?.inputPower1
                        57 -> second?.inputCurrent1
                        58 -> second?.dcRunState1
                        59 -> second?.dcAccessType1
                        60 -> second?.dcFaultStatus1
                        61 -> second?.highSideVoltage1


                        62 -> getString(R.string.m235_mppt_data_2)
                        63 -> second?.inputVoltage2
                        64 -> second?.inputTemp2
                        65 -> second?.inputPower2
                        66 -> second?.inputCurrent2
                        67 -> second?.dcRunState2
                        68 -> second?.dcAccessType2
                        69 -> second?.dcFaultStatus2
                        70 -> second?.highSideVoltage2


                        71 -> getString(R.string.m236_mppt_data_3)
                        72 -> second?.inputVoltage3
                        73 -> second?.inputTemp3
                        74 -> second?.inputPower3
                        75 -> second?.inputCurrent3
                        76 -> second?.dcRunState3
                        77 -> second?.dcAccessType3
                        78 -> second?.dcFaultStatus3
                        79 -> second?.highSideVoltage3


                        80 -> getString(R.string.m237_mppt_data_4)
                        81 -> second?.inputVoltage4
                        82 -> second?.inputTemp4
                        83 -> second?.inputPower4
                        84 -> second?.inputCurrent4
                        85 -> second?.dcRunState4
                        86 -> second?.dcAccessType4
                        87 -> second?.dcFaultStatus4
                        88 -> second?.highSideVoltage4


                        89 -> getString(R.string.m213_bat_data)
                        90 -> second?.voltage
                        91 -> second?.runStatus
                        92 -> second?.soc
                        93 -> second?.current
                        94 -> second?.maxChargeCurrent
                        95 -> second?.soh
                        96 -> second?.maxCellVoltege
                        97 -> second?.maxDischargeCurrent
                        98 -> second?.maxCellTemp
                        99 -> second?.minCellTemp
                        100 -> second?.minCellVoltage

                        else -> {
                            ""
                        }
                    }
                    val isTitle =
                        (i == 5) || (i == 17) || (i == 29) || (i == 41) || (i == 53)
                                || (i == 62) || (i == 71) || (i == 80) || (i == 89)

                    val model = PcsDataModel(isTitle, title, value.toString(), title)
                    datas.add(model)
                }
                (binding.rlvData.adapter as Adapter).refresh(datas)

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
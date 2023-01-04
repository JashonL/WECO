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


            getString(R.string.m203_mppt_data_1),
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
        binding.rlvData.layoutManager = GridLayoutManager(this,2)
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
                return if (position == 5||position==17||position==26) {
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


                        17 -> getString(R.string.m203_mppt_data_1)
                        18 -> second?.inputVoltage1
                        19 -> second?.inputTemp1
                        20 -> second?.inputPower1
                        21 -> second?.inputCurrent1
                        22 -> second?.dcRunState1
                        23 -> second?.dcAccessType1
                        24 -> second?.dcFaultStatus1
                        25 -> second?.highSideVoltage1


                        26 -> getString(R.string.m213_bat_data)
                        27 -> second?.voltage
                        28 -> second?.runStatus
                        29 -> second?.soc
                        30 -> second?.current
                        31 -> second?.maxChargeCurrent
                        32 -> second?.soh
                        33 -> second?.maxCellVoltege
                        34 -> second?.maxDischargeCurrent
                        35 -> second?.maxCellTemp
                        36 -> second?.minCellTemp
                        37 -> second?.minCellVoltage

                        else -> {
                            ""
                        }
                    }
                    val isTitle = (i == 5) || (i == 17) || (i == 26)
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
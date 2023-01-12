package com.olp.weco.ui.message

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.olp.weco.R
import com.olp.weco.base.BaseActivity
import com.olp.weco.base.BasePageListAdapter
import com.olp.weco.base.BaseViewHolder
import com.olp.weco.base.OnItemClickListener
import com.olp.weco.base.viewholder.EmptyViewHolder
import com.olp.weco.databinding.ActivityMessageBinding
import com.olp.weco.databinding.ItemMessageBinding
import com.olp.weco.ui.device.adapter.DeviceAdapter
import com.olp.weco.ui.device.viewholder.BaseDeviceViewHolder
import com.olp.weco.ui.message.model.HmiDataFaultMessage
import com.olp.weco.ui.message.viewmodel.MessageViewModel
import com.olp.weco.ui.station.viewmodel.StationViewModel
import com.olp.weco.view.itemdecoration.DividerItemDecoration
import com.olp.weco.view.pop.ListPopuwindow
import com.olp.weco.view.popuwindow.ListPopModel

class MessageActivity : BaseActivity() {

    companion object {
        fun start(context: Context, plantId: String) {
            val intent = Intent(context, MessageActivity::class.java)
            intent.putExtra("plantId", plantId)
            context.startActivity(intent)
        }
    }

    private lateinit var _binding: ActivityMessageBinding


    //绑定电站viewmodel
    private val viewModel: StationViewModel by viewModels()


    private val messageViewModel: MessageViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        setliseners()
        initViews()
        initData()
    }


    private fun setliseners() {
        _binding.title.setOnLeftIconClickListener {
            finish()
        }
        _binding.srlRefresh.setOnRefreshListener {
            showDeviceList()
        }
        _binding.title.setOnTitleClickListener {
            showPlantList()
        }
    }


    private fun showPlantList() {
        val second = viewModel.getPlantListLiveData.value?.second
        if (second == null || second.isEmpty()) {
            fetchPlantList()
        } else {
            val options = mutableListOf<ListPopModel>()
            for (plant in second) {
                options.add(ListPopModel(plant.plantName.toString(), false))
            }

            val curItem: String? = if (viewModel.currentStation != null) {
                viewModel.currentStation!!.id
            } else {
                ""
            }
            ListPopuwindow.showPop(
                this,
                options,
                _binding.title.getTitilView(),
                curItem ?: ""
            ) {
                _binding.title.setTitleText(second[it].plantName)
                //请求设备列表
                messageViewModel.currentPlantId = second[it].id
                showDeviceList()
            }
        }
    }


    private fun initViews() {
        _binding.rlvMessageList.layoutManager = LinearLayoutManager(this)
        _binding.rlvMessageList.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL,
                resources.getColor(R.color.nocolor),
                10f
            )
        )
        _binding.rlvMessageList.adapter = DeviceAdapter()


    }


    private fun initData() {
        messageViewModel.currentPlantId = intent.getStringExtra("plantId")

        //请求设备列表
        messageViewModel.deviceListLiveData.observe(this) {
            dismissDialog()
            if (it.first) {
                val second = it.second
                (_binding.rlvMessageList.adapter as DeviceAdapter).refresh(second?.toList())
            }
        }


        viewModel.getPlantListLiveData.observe(this) {
            dismissDialog()
            val second = it.second
            if (!second.isNullOrEmpty()) {
                //获取从外面传进来的电站id
                val plantId = intent.getStringExtra("plantId")
                for (i in second.indices) {
                    val plantModel = second[i]
                    if (plantModel.id == plantId) {
                        viewModel.currentStation = plantModel
                        break
                    }
                }

                //设置当前选中
                val name = viewModel.currentStation?.plantName
                messageViewModel.currentPlantId = viewModel.currentStation?.id
                _binding.title.setTitleText(name)

                //请求设备列表
                showDeviceList()

            }
        }


        //请求电站列表
        fetchPlantList()

    }

    fun showDeviceList() {
        _binding.srlRefresh.finishRefresh()
        showDialog()
        messageViewModel.getMessageList(0)
    }


    private fun fetchPlantList() {
        showDialog()
        viewModel.getPlantList()
    }


    private fun getMessageList(curentPage: Int) {
        messageViewModel.getMessageList(curentPage)
    }


    inner class Adapter : BasePageListAdapter<HmiDataFaultMessage>(), OnItemClickListener {


        override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
            return if (viewType == DeviceAdapter.ADAPTER_DATA_EMPTY) {
                EmptyViewHolder.create(parent)
            } else {
                RecordViewHolder.create(parent, this)

            }
        }

        override fun onBindItemViewHolder(holder: BaseViewHolder, position: Int) {
            if (holder is RecordViewHolder) {
                holder.bindData(dataList[position])
            }
        }

        override fun onLoadNext() {
            getMessageList(++currentPage)
        }

        override fun onRefresh() {
            getMessageList(currentPage)
        }


        override fun showEmptyView() {
        }

        override fun hideEmptyView() {
        }


        override fun getItemCount(): Int {
            if (dataList.isEmpty() || isRefreshing) {
                return 1
            }
            return 1 + dataList.size
        }


        override fun getItemViewType(position: Int): Int {
            if (dataList.isEmpty()) {
                return ADAPTER_DATA_EMPTY
            }
            return super.getItemViewType(position)
        }

    }


    class RecordViewHolder(
        itemView: View,
    ) : BaseViewHolder(itemView) {

        companion object {
            fun create(
                parent: ViewGroup,
                onItemClickListener: OnItemClickListener?
            ): RecordViewHolder {
                val binding = ItemMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                val holder = RecordViewHolder(binding.root)
                holder.binding = binding
                holder.binding.root.setOnClickListener(holder)
                return holder
            }
        }

        private lateinit var binding: ItemMessageBinding

        fun bindData(message: HmiDataFaultMessage) {
            binding.tvTime.text = message.faultTime
            binding.llReadStatus.visibility =
                if (message.readStatus == 0) View.INVISIBLE else View.VISIBLE




        }
    }

}
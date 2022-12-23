package com.olp.weco.ui.station.fragment

import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.olp.weco.ui.station.monitor.PlantMonitor
import com.olp.weco.R
import com.olp.weco.base.BaseFragment
import com.olp.weco.base.BaseViewHolder
import com.olp.weco.base.OnItemClickListener
import com.olp.weco.databinding.FragmentPlantListBinding
import com.olp.weco.databinding.PlantViewHolderBinding
import com.olp.weco.model.PlantModel
import com.olp.weco.ui.MainActivity
import com.olp.weco.ui.home.viewmodel.PlantFilterViewModel
import com.olp.weco.ui.station.activity.AddPlantActivity
import com.olp.weco.ui.station.activity.AddTtchPlantActivity
import com.olp.weco.ui.station.viewmodel.PlantInfoViewModel
import com.olp.weco.ui.station.viewmodel.PlantListViewModel
import com.olp.weco.ui.station.viewmodel.StationViewModel
import com.olp.weco.utils.ValueUtil
import com.olp.weco.view.itemdecoration.DividerItemDecoration
import com.olp.lib.util.ToastUtil
import com.olp.lib.util.ViewUtil
import com.olp.lib.util.gone
import com.olp.lib.util.visible

/**
 * 电站列表
 */
class PlantListFragment : BaseFragment() {


    private var plantStatus: Int? = null
    private var searchWord: String? = null


    private var _binding: FragmentPlantListBinding? = null

    private val binding get() = _binding!!
    private val viewModel: PlantListViewModel by viewModels()
    private val filterViewModel: PlantFilterViewModel by activityViewModels()
    private val plantInfoViewModel: PlantInfoViewModel by viewModels()

    private val stationViewModel: StationViewModel by viewModels()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        val arguments = arguments
        plantStatus = arguments?.let {
            val status = it.getInt("status")
            status
        }

        searchWord = arguments?.let {
            val searchWord = it.getString("searchWord")
            searchWord
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlantListBinding.inflate(inflater, container, false)
        setListener()
        initData()
        initView()
        return binding.root
    }

    private fun setListener() {
        binding.srfRefresh.setOnRefreshListener {
            refresh()
        }
        binding.ivAddPlant.setOnClickListener {
            AddTtchPlantActivity.start(requireContext())
        }
    }

    private fun initData() {
        viewModel.getPlantListLiveData.observe(viewLifecycleOwner) {
            binding.srfRefresh.finishRefresh()
            if (it.second == null) {
                getAdapter()?.refresh(it.first)
                refreshEmptyView(it.first)
            } else {
                ToastUtil.show(it.second)
            }
        }
        viewModel.getPlantStatusNumLiveData.observe(viewLifecycleOwner) {
            if (it.second == null) {
//                listener.onPlantStatusNumChange(it.first)
            }
        }
        filterViewModel.getPlantFilterLiveData.observe(viewLifecycleOwner) {
            binding.srfRefresh.autoRefresh()
        }
        viewModel.deletePlantLiveData.observe(viewLifecycleOwner) {
            dismissDialog()
            if (it == null) {
                PlantMonitor.notifyPlant()
            } else {
                ToastUtil.show(it)
            }
        }
        plantInfoViewModel.getPlantInfoLiveData.observe(viewLifecycleOwner) {
            dismissDialog()
            if (it.second == null) {
                AddPlantActivity.start(requireContext(), it.first)
            } else {
                ToastUtil.show(it.second)
            }
        }
        PlantMonitor.watch(viewLifecycleOwner.lifecycle) {
            binding.srfRefresh.autoRefresh()
        }

    }

    private fun refreshEmptyView(plantModels: Array<PlantModel>?) {
        if (plantModels.isNullOrEmpty()) {
            binding.llEmpty.visible()
            if (plantStatus == PlantModel.PLANT_STATUS_ALL) {
                binding.ivAddPlant.visible()
                binding.tvEmptyText.text = getString(R.string.m44_add_plant)
            } else {
                binding.ivAddPlant.gone()
                binding.tvEmptyText.text =
                    getString(R.string.m81_nothing_plant_format, getPlantStatusText())
            }
        } else {
            binding.llEmpty.gone()
        }
    }

    private fun refresh() {
        filterViewModel.getPlantFilterLiveData.value?.let {
            viewModel.getPlantList(plantStatus ?: 0, it, searchWord.toString())
        }
    }

    private fun initView() {
        binding.rvPlant.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                resources.getColor(R.color.color_line)
            )
        )
        binding.rvPlant.adapter = Adapter()
        //item高度固定的时候，设置这个优化性能
        binding.rvPlant.setHasFixedSize(true)

    }

    private fun getAdapter(): Adapter? {
        if (binding.rvPlant.adapter is Adapter) {
            return binding.rvPlant.adapter as Adapter
        }
        return null
    }

    private fun getPlantStatusText(): String {
        return when (plantStatus) {
            PlantModel.PLANT_STATUS_OFFLINE -> getString(R.string.m38_offline)
            PlantModel.PLANT_STATUS_ONLINE -> getString(R.string.m37_online)
            PlantModel.PLANT_STATUS_TROUBLE -> getString(R.string.m39_trouble)
            else -> getString(R.string.m36_all)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * ListAdapter需要搭配DiffUtil一起使用
     */
    inner class Adapter : ListAdapter<PlantModel, PlantViewHolder>(DiffCallback()),
        OnItemClickListener {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
            return PlantViewHolder.create(parent, this)
        }

        override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
            holder.bindData(getItem(position), this@PlantListFragment)
        }

        override fun onItemClick(v: View?, position: Int) {
            (activity as MainActivity).showHomeFragment(getItem(position))
        }

        override fun onItemLongClick(v: View?, position: Int) {

        }

        fun refresh(plantModels: Array<PlantModel>?) {
            submitList(plantModels?.toList())
        }
    }

    class PlantViewHolder(
        itemView: View,
        onItemClickListener: OnItemClickListener,
    ) :
        BaseViewHolder(itemView, onItemClickListener) {

        companion object {
            fun create(
                parent: ViewGroup,
                onItemClickListener: OnItemClickListener,
            ): PlantViewHolder {
                val binding = PlantViewHolderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                val holder = PlantViewHolder(binding.root, onItemClickListener)
                binding.llCity.background =
                    ViewUtil.createShape(holder.getColor(R.color.color_33000000), 0, 0, 2, 2)

                holder.binding = binding
                binding.root.setOnClickListener(holder)
                binding.root.setOnLongClickListener(holder)
                return holder
            }
        }

        private lateinit var binding: PlantViewHolderBinding

        fun bindData(plantModel: PlantModel, fragment: PlantListFragment) {

            Glide.with(fragment).load(plantModel.pictureAddress)
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.ivPlantImage)

            binding.llCity.background =
                ViewUtil.createShape(getColor(R.color.color_33000000), 0, 0, 2, 2)
            binding.tvCity.text = plantModel.city
            binding.tvPlantName.text = plantModel.stationName
            when (plantModel.onlineStatus) {
                PlantModel.PLANT_STATUS_ONLINE -> {
                    binding.tvPlantStatus.text = getString(R.string.m37_online)
                    binding.tvPlantStatus.background =
                        ViewUtil.createShape(getColor(R.color.color_green), 8)
                }
                PlantModel.PLANT_STATUS_OFFLINE -> {
                    binding.tvPlantStatus.text = getString(R.string.m38_offline)
                    binding.tvPlantStatus.background =
                        ViewUtil.createShape(getColor(R.color.color_bbbbbb), 8)
                }
                PlantModel.PLANT_STATUS_TROUBLE -> {
                    binding.tvPlantStatus.text = getString(R.string.m39_trouble)
                    binding.tvPlantStatus.background =
                        ViewUtil.createShape(getColor(R.color.color_FF6434), 8)
                }
            }
            binding.llCurrentPower.background =
                ViewUtil.createShape(getColor(R.color.color_1A0087FD), 2)
            binding.tvCurrentPower.text = plantModel.currencyPower
            binding.tvInstallDate.text = plantModel.installtionDate
            binding.tvTotalComponentPower.text =ValueUtil.valueFromWp(plantModel.pvcapacity.toDouble()).first
            binding.tvPower.text = getTvSpan(plantModel)
            binding.root.tag = plantModel
        }

        private fun getTvSpan(plantModel: PlantModel): SpannableString {
            return SpannableString(
                "${plantModel.getETodayText()}/${plantModel.getETotalText()}${
                    getString(
                        R.string.m48_kwh
                    )
                }"
            ).also {
                val span = ForegroundColorSpan(getColor(R.color.text_gray_99))
                val startPosition = it.toString().indexOf("/")
                val endPosition = startPosition + "/".length
                it.setSpan(span, startPosition, endPosition, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<PlantModel>() {
        /**
         * 是不是相同item
         */
        override fun areItemsTheSame(oldItem: PlantModel, newItem: PlantModel): Boolean {
            return oldItem.id == newItem.id
        }

        /**
         * item内容是否相等
         */
        override fun areContentsTheSame(oldItem: PlantModel, newItem: PlantModel): Boolean {
            return oldItem == newItem
        }

    }

}
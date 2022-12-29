package com.olp.weco.ui.device.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.olp.weco.base.OnItemClickListener
import com.olp.weco.databinding.HvBatBoxHolderBinding
import com.olp.weco.databinding.Xte8kHolderBinding
import com.olp.weco.model.DeviceModel

class XPViewHolder(
    itemView: View,
    onItemClickListener: OnItemClickListener
) : BaseDeviceViewHolder(itemView, onItemClickListener) {


    companion object {
        fun create(
            parent: ViewGroup,
            onItemClickListener: OnItemClickListener
        ): XPViewHolder {
            val binding = Xte8kHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            val holder = XPViewHolder(binding.root, onItemClickListener)
            holder.binding = binding
            binding.root.setOnClickListener(holder)
            return holder
        }
    }

    private lateinit var binding: Xte8kHolderBinding

    override fun bindData(deviceModel: DeviceModel) {
        binding.tvDeviceName.text = deviceModel.deviceName
//        binding.tvStatus.text = deviceModel.deviceName
//        binding.tvBatPercent.text = deviceModel.deviceName
        binding.tvDeviceType.text = deviceModel.deviceTypeText
    }
}
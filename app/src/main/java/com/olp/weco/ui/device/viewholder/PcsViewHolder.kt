package com.olp.weco.ui.device.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.olp.weco.base.OnItemClickListener
import com.olp.weco.databinding.HvBatBoxHolderBinding
import com.olp.weco.databinding.PcsViewHolderBinding
import com.olp.weco.databinding.Xte8kHolderBinding
import com.olp.weco.model.DeviceModel

class PcsViewHolder(
    itemView: View,
    onItemClickListener: OnItemClickListener
) : BaseDeviceViewHolder(itemView, onItemClickListener) {


    companion object {
        fun create(
            parent: ViewGroup,
            onItemClickListener: OnItemClickListener
        ): PcsViewHolder {
            val binding = PcsViewHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            val holder = PcsViewHolder(binding.root, onItemClickListener)
            holder.binding = binding
            binding.root.setOnClickListener(holder)
            return holder
        }
    }

    private lateinit var binding: PcsViewHolderBinding

    override fun bindData(deviceModel: DeviceModel) {
        binding.tvDeviceName.text = deviceModel.deviceName
//        binding.tvStatus.text = deviceModel.deviceName
//        binding.tvBatPercent.text = deviceModel.deviceName
        binding.tvDeviceType.text = deviceModel.deviceTypeText
    }
}
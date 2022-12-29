package com.olp.weco.ui.device.viewholder

import android.view.View
import android.view.ViewGroup
import com.olp.weco.base.BaseViewHolder
import com.olp.weco.base.OnItemClickListener
import com.olp.weco.model.DeviceModel
import com.olp.weco.model.DeviceType

/**
 * 设备ViewHolder基类
 */
abstract class BaseDeviceViewHolder(
    itemView: View,
    onItemClickListener: OnItemClickListener
) : BaseViewHolder(itemView, onItemClickListener) {

    companion object {
        fun createDeviceViewHolder(
            @DeviceType deviceType: Int, parent: ViewGroup,
            onItemClickListener: OnItemClickListener,
        ): BaseDeviceViewHolder {
            return when (deviceType) {
                DeviceType.PCS -> PcsViewHolder.create(parent, onItemClickListener)
                DeviceType.XP -> XPViewHolder.create(parent, onItemClickListener)
                else -> HvbatboxViewHolder.create(parent, onItemClickListener)
            }
        }
    }

    abstract fun bindData(deviceModel: DeviceModel)
}
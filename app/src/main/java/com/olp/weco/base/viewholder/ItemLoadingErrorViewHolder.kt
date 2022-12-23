package com.olp.weco.base.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.olp.weco.base.BaseViewHolder
import com.olp.weco.databinding.ItemLoadingErrorViewHolderBinding

/**
 * 加载失败
 */
class ItemLoadingErrorViewHolder(itemView: View) : BaseViewHolder(itemView) {

    companion object {
        fun create(
            parent: ViewGroup,
            onClickListener: View.OnClickListener
        ): ItemLoadingErrorViewHolder {
            val binding = ItemLoadingErrorViewHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            val holder = ItemLoadingErrorViewHolder(binding.root)
            holder.binding = binding
            binding.root.setOnClickListener(onClickListener)
            return holder
        }
    }

    private lateinit var binding: ItemLoadingErrorViewHolderBinding
}
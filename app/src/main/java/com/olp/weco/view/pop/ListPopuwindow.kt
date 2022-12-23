package com.olp.weco.view.pop

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.olp.weco.R
import com.olp.weco.base.BaseViewHolder
import com.olp.weco.base.OnItemClickListener
import com.olp.weco.databinding.ListPopLayoutBinding
import com.olp.weco.databinding.PopListItemBinding
import com.olp.weco.view.popuwindow.ListPopModel
import com.olp.lib.util.gone
import com.olp.lib.util.visible


/**
 * 下拉列表 pop
 * 目前只支持单选
 */

class ListPopuwindow(
    context: Context,
    list: List<ListPopModel>,
    curItem: String,
    chooseLisener: ((pos: Int) -> Unit)? = null
) :
    PopupWindow(), OnItemClickListener {

    private var chooseLisener: ((pos: Int) -> Unit)? = null


    companion object {
        fun showPop(
            context: Context, list: List<ListPopModel>,
            dropView: View, curItem: String, chooselisener: ((pos: Int) -> Unit)? = null
        ) {
            ListPopuwindow(context, list, curItem, chooselisener).showAsDropDown(dropView)
        }
    }


    var binding: ListPopLayoutBinding


    init {
        val inflater = LayoutInflater.from(context)
//        this.contentView = inflater.inflate(R.layout.list_pop_layout, null) //布局xml
//        ListPopLayoutBinding.bind(this.contentView)
        binding = ListPopLayoutBinding.inflate(inflater)
        this.contentView = binding.root

        initContentView(context, list, curItem)

        this.width = LinearLayout.LayoutParams.WRAP_CONTENT //父布局减去padding
        this.height = LinearLayout.LayoutParams.WRAP_CONTENT
        this.animationStyle = R.style.Popup_Anim  //进入和退出动画效果
        this.isOutsideTouchable = true //是否可以
        this.isClippingEnabled = false //背景透明化可以铺满全屏
        // 设置最终的背景,也可以通过context.resources.getColor(resId)设置自己的颜色
        val colorDrawable = ColorDrawable(Color.parseColor("#00000000"))
        this.setBackgroundDrawable(colorDrawable) //设置背景
        this.setTouchInterceptor { _, _ -> false };
        isTouchable = true
        this.isFocusable = true
        this.chooseLisener = chooseLisener
    }


    private fun initContentView(context: Context, list: List<ListPopModel>, curItem: String) {
        for (i in list.indices) {
            list[i].choose = list[i].title == curItem
        }
        binding.rlvList.layoutManager = LinearLayoutManager(context)
        binding.rlvList.adapter = ListAdapter(context, list.toMutableList(), this)
    }


    override fun onItemClick(v: View?, position: Int) {
        super.onItemClick(v, position)
        if (chooseLisener != null) {
            chooseLisener?.invoke(position)
        }

        this.dismiss()
    }


    fun setOnChooseListener(listener: ((pos: Int) -> Unit)) {
        this.chooseLisener = listener
    }


    //dapter

    class ListAdapter(
        val context: Context,
        private val list: MutableList<ListPopModel>,
        val listener: OnItemClickListener
    ) :
        RecyclerView.Adapter<ListViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
            return ListViewHolder.create(context, listener)
        }

        override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
            val item = list[position]
            holder.bindData(item, position)
        }

        override fun getItemCount(): Int {
            return list.size
        }


/*
        override fun onItemClick(v: View?, position: Int) {
            super.onItemClick(v, position)
            itemChoose(position)
        }

        @SuppressLint("NotifyDataSetChanged")
        private fun itemChoose(position: Int) {
            //将所有选项设置成未选
            for (i in list.indices) {
                list[i].choose = i == position
            }
            notifyDataSetChanged()
        }
*/

    }


    class ListViewHolder(itemView: View, onItemClickListener: OnItemClickListener? = null) :
        BaseViewHolder(itemView, onItemClickListener) {

        lateinit var binding: PopListItemBinding

        companion object {
            fun create(
                context: Context,
                onItemClickListener: OnItemClickListener?
            ): ListViewHolder {
                val view = LayoutInflater.from(context).inflate(R.layout.pop_list_item, null)
                val bind = PopListItemBinding.bind(view)
                val listViewHolder = ListViewHolder(view, onItemClickListener)
                listViewHolder.binding = bind
                listViewHolder.binding.root.setOnClickListener(listViewHolder)
                listViewHolder.binding.root.setOnLongClickListener(listViewHolder)
                return listViewHolder
            }
        }


        fun bindData(listPopModel: ListPopModel, position: Int) {
            val choose = listPopModel.choose
            if (choose) binding.ivChoose.visible() else binding.ivChoose.gone()
            binding.tvItem.text = listPopModel.title
        }


    }


}
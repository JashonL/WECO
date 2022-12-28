package com.olp.weco.view

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.olp.weco.R
import com.olp.weco.databinding.TabItemBinding

/**
 * 自定义组合View-Tab
 */
class Tab @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr),TabSelect {

    private val binding: TabItemBinding

    private var isSelect = false


    private var icon: Drawable? = null

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.tab_item, this)
        binding = TabItemBinding.bind(view)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.Tab,
            0, 0
        ).apply {
            try {
                icon = getDrawable(R.styleable.Tab_tab_icon)
            } finally {
                recycle()
            }

        }


        initView()
    }

    private fun initView() {
        if (icon != null) {
            binding.ivIcon.setImageDrawable(icon)
        }
        updateView(isSelect)
    }


    override fun setSelect(isSelect: Boolean) {
        if (this.isSelect == isSelect) {
            return
        }
        updateView(isSelect)
    }

    override fun isSelect(): Boolean {
        return isSelect
    }

    private fun updateView(isSelect: Boolean) {
        this.isSelect = isSelect
        if (isSelect) {
            binding.ivIcon.setBackgroundResource(R.drawable.stroke_gray)
        } else {
            binding.ivIcon.setBackgroundResource(0)
        }
    }





}
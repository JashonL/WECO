package com.olp.weco.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.databinding.adapters.TextViewBindingAdapter.setTextSize
import com.olp.weco.R
import com.olp.weco.databinding.TabItemBinding
import com.olp.weco.databinding.TextTabItemBinding

/**
 * 自定义组合View-Tab
 */
class TextTab @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) ,TabSelect{

    private val binding: TextTabItemBinding

    private var tabText: String? = null

    private var isSelect = false

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.text_tab_item, this)
        binding = TextTabItemBinding.bind(view)
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.Tab,
            0, 0
        ).apply {
            try {
                tabText = getString(R.styleable.Tab_tabText) ?: ""
            } finally {
                recycle()
            }
        }
        initView()
    }

    private fun initView() {
        binding.tvTab.text = tabText
        updateView(isSelect)
    }

    fun setTabText(tabText: String?) {
        this.tabText = tabText
        binding.tvTab.text = tabText ?: ""
    }

    fun getTabText(): String {
        return tabText ?: ""
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
        binding.tvTab.apply {
            typeface = if (isSelect) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
            setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                context.resources.getDimension(if (isSelect) R.dimen.text_subtitle else R.dimen.text_medium)
            )
            setTextColor(context.resources.getColor(if (isSelect) R.color.text_black else R.color.text_gray_99))
        }
        binding.indicator.visibility = if (isSelect) VISIBLE else INVISIBLE
    }
}
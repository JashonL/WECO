package com.olp.weco.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2

/**
 * 自定义View-TabLayout
 */
class TabLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private var selectTabPosition = -1

    private val listeners = mutableSetOf<OnTabSelectedListener>()

    private var viewPager2: ViewPager2? = null

    init {
        orientation = HORIZONTAL
    }

    /**
     * @param isTriggerCallback 是否触发回调
     */
    fun setSelectTabPosition(selectTabPosition: Int, isTriggerCallback: Boolean = true) {
        if (this.selectTabPosition == selectTabPosition) {
            return
        }
        this.selectTabPosition = selectTabPosition
        updateView(selectTabPosition)
        if (isTriggerCallback) {
            dispatch(selectTabPosition)
        }
        viewPager2?.currentItem = selectTabPosition
    }

    private fun dispatch(selectTabPosition: Int) {
        for (listener in listeners) {
            val tab = getChildAt(selectTabPosition)
            if (tab is Tab){
                listener.onTabSelect(tab, selectTabPosition)
            }else if (tab is TextTab){
                listener.onTabSelect(tab, selectTabPosition)
            }

        }
    }

    private fun updateView(selectTabPosition: Int) {
        for (index in 0 until childCount) {
            val tab = getChildAt(index)
            if (tab is TabSelect) {
                if (index == selectTabPosition && !tab.isSelect()) {
                    tab.setSelect(true)
                } else if (index != selectTabPosition && tab.isSelect()) {
                    tab.setSelect(false)
                }
            }
        }
    }

    fun addTabSelectedListener(listener: OnTabSelectedListener) {
        listeners.add(listener)
    }

    fun removeTabSelectedListener(listener: OnTabSelectedListener) {
        listeners.remove(listener)
    }

    override fun addView(child: View?) {
        if (child !is TabSelect) {
            throw IllegalArgumentException("Only Tab instances can be added to TabLayout")
        }
        super.addView(child)
        child.setOnClickListener(this)
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        if (child !is TabSelect) {
            throw IllegalArgumentException("Only Tab instances can be added to TabLayout")
        }
        super.addView(child, params)
        child.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        setSelectTabPosition(indexOfChild(v))
    }

    fun setupWithViewPager2(viewPager2: ViewPager2) {
        this.viewPager2 = viewPager2
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setSelectTabPosition(position, false)
            }
        })
    }

    fun setTabText(tabText: String, position: Int) {

    }

    fun getSelectTabPosition(): Int {
        return selectTabPosition
    }


}

interface OnTabSelectedListener {
    fun onTabSelect(selectTab: Tab, selectPosition: Int)
    fun onTabSelect(selectTab: TextTab, selectPosition: Int)
}


interface TabSelect {
    fun isSelect(): Boolean
    fun setSelect(isSelect: Boolean)
}
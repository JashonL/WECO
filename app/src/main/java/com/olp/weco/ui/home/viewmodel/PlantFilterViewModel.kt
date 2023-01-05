package com.olp.weco.ui.home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.olp.weco.base.BaseViewModel

/**
 * 电站筛选
 */
class PlantFilterViewModel : BaseViewModel() {

    val getPlantFilterLiveData = MutableLiveData<Int>()
    val getPlantSearchLiveData = MutableLiveData<String>()




    /**
     * 设置排序类型
     */
    fun setFilterType(orderType: Int) {
        getPlantFilterLiveData.value = orderType
    }


    /**
     * 设置排序类型
     */
    fun setSearchWord(searchWord: String) {
        getPlantSearchLiveData.value = searchWord
    }

}
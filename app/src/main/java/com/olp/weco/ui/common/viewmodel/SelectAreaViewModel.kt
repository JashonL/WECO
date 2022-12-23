package com.olp.weco.ui.common.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.olp.weco.base.BaseViewModel
import com.olp.weco.service.http.ApiPath
import com.olp.lib.service.http.HttpCallback
import com.olp.lib.service.http.HttpErrorModel
import com.olp.lib.service.http.HttpResult
import kotlinx.coroutines.launch

/**
 * 选择国家/地区
 */
class SelectAreaViewModel : BaseViewModel() {

    val areaListLiveData = MutableLiveData<Pair<Array<String>, String?>>()

    /**
     * 获取国家/地区列表
     */
    fun fetchAreaList() {
        viewModelScope.launch {
            apiService().post(
                ApiPath.Commom.GETCOUNTRYLIST,
                object : HttpCallback<HttpResult<Array<String>>>() {
                    override fun success(result: HttpResult<Array<String>>) {
                        if (result.isBusinessSuccess()) {
                            val countryList = result.obj
                            if (countryList == null) {
                                areaListLiveData.value = Pair(emptyArray(), null)
                            } else {
                                areaListLiveData.value = Pair(countryList, null)
                            }
                        } else {
                            areaListLiveData.value = Pair(emptyArray(), result.msg ?: "")
                        }
                    }

                    override fun onFailure(errorModel: HttpErrorModel) {
                        areaListLiveData.value = Pair(emptyArray(), errorModel.errorMsg ?: "")
                    }

                })
        }
    }
}
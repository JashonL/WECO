package com.olp.weco.ui.home.pv.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.olp.weco.base.BaseViewModel
import com.olp.weco.service.http.ApiPath
import com.olp.weco.ui.home.model.InvModel
import com.olp.lib.service.http.HttpCallback
import com.olp.lib.service.http.HttpErrorModel
import com.olp.lib.service.http.HttpResult
import kotlinx.coroutines.launch

/**
 * 请求电站状态
 */
class PvStationViewmodel : BaseViewModel() {


    val statusLiveData = MutableLiveData<InvModel?>()

     var stationId: String? = null

    /**
     * 获取系统状态
     */
    fun getDataOverview() {
        val params = hashMapOf<String, String>().apply {
            put("stationId", stationId ?: "")
        }

        viewModelScope.launch {
            apiService().postForm(
                ApiPath.Plant.GETDATAOVERVIEW, params,
                object : HttpCallback<HttpResult<InvModel>>() {
                    override fun success(result: HttpResult<InvModel>) {
                        if (result.isBusinessSuccess()) {
                            statusLiveData.value = result.obj
                        } else {
                            statusLiveData.value = null
                        }
                    }

                    override fun onFailure(errorModel: HttpErrorModel) {
                        statusLiveData.value = null
                    }

                })
        }
    }


}
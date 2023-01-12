package com.olp.weco.ui.message.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.olp.lib.service.http.HttpCallback
import com.olp.lib.service.http.HttpErrorModel
import com.olp.lib.service.http.HttpResult
import com.olp.weco.base.BaseViewModel
import com.olp.weco.model.DeviceModel
import com.olp.weco.service.http.ApiPath
import kotlinx.coroutines.launch

class MessageViewModel:BaseViewModel() {


    val deviceListLiveData = MutableLiveData<Pair<Boolean, Array<DeviceModel>?>>()


    var currentPlantId:String? = ""



    /**
     * 获取电站数据
     */
    fun getMessageList(page: Int) {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("plantId", currentPlantId.toString())
            }
            apiService().postForm(
                ApiPath.FaultMessage.getFaultMessage(page),
                params,
                object : HttpCallback<HttpResult<Array<DeviceModel>>>() {
                    override fun success(result: HttpResult<Array<DeviceModel>>) {
                        deviceListLiveData.value = Pair(result.isBusinessSuccess(), result.obj)
                    }

                    override fun onFailure(errorModel: HttpErrorModel) {
                        deviceListLiveData.value = Pair(false, null)

                    }
                })
        }
    }


}
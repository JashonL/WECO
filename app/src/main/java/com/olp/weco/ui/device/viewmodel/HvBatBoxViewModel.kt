package com.olp.weco.ui.device.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.olp.lib.service.http.HttpCallback
import com.olp.lib.service.http.HttpErrorModel
import com.olp.lib.service.http.HttpResult
import com.olp.weco.base.BaseViewModel
import com.olp.weco.model.DeviceType
import com.olp.weco.service.http.ApiPath
import com.olp.weco.ui.device.model.HvBox
import kotlinx.coroutines.launch

class HvBatBoxViewModel : BaseViewModel() {


    val hvBatBoxLiveData = MutableLiveData<Pair<Boolean, HvBox?>>()


    var deviceType: Int = DeviceType.HVBOX

    var deviceSn: String = ""


    /**
     * 获取电站数据
     */
    fun getdeviceDetails() {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("deviceType", deviceType.toString())
                put("deviceSn", deviceSn)

            }
            apiService().postForm(
                ApiPath.Device.GETDEVICEDETAILS,
                params,
                object : HttpCallback<HttpResult<HvBox>>() {
                    override fun success(result: HttpResult<HvBox>) {
                        hvBatBoxLiveData.value = Pair(result.isBusinessSuccess(), result.obj)
                    }

                    override fun onFailure(errorModel: HttpErrorModel) {
                        hvBatBoxLiveData.value = Pair(false, null)

                    }
                })
        }
    }


}
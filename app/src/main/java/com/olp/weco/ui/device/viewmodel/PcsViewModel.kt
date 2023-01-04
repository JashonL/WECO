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
import com.olp.weco.ui.device.model.PCS
import kotlinx.coroutines.launch

class PcsViewModel : BaseViewModel() {


    val pcsLiveData = MutableLiveData<Pair<Boolean, PCS?>>()


    var deviceType: Int = DeviceType.PCS

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
                object : HttpCallback<HttpResult<PCS>>() {
                    override fun success(result: HttpResult<PCS>) {
                        pcsLiveData.value = Pair(result.isBusinessSuccess(), result.obj)
                    }

                    override fun onFailure(errorModel: HttpErrorModel) {
                        pcsLiveData.value = Pair(false, null)

                    }
                })
        }
    }


}
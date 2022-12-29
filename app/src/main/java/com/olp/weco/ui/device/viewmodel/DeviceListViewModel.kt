package com.olp.weco.ui.device.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.olp.lib.service.http.HttpCallback
import com.olp.lib.service.http.HttpErrorModel
import com.olp.lib.service.http.HttpResult
import com.olp.weco.base.BaseViewModel
import com.olp.weco.model.DeviceModel
import com.olp.weco.model.ImpactModel
import com.olp.weco.model.PlantModel
import com.olp.weco.service.http.ApiPath
import com.olp.weco.ui.chart.ChartListDataModel
import com.olp.weco.ui.chart.ChartYDataList
import kotlinx.coroutines.launch

class DeviceListViewModel : BaseViewModel() {

    val deviceListLiveData = MutableLiveData<Pair<Boolean, Array<DeviceModel>?>>()


    var currentPlantId:String? = ""

    /**
     * 获取电站数据
     */
    fun getDevicelist() {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("plantId", currentPlantId.toString())
            }
            apiService().postForm(
                ApiPath.Device.GETDEVICELIST,
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
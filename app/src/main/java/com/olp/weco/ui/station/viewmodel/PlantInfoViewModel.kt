package com.olp.weco.ui.station.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.olp.weco.base.BaseViewModel
import com.olp.weco.model.DeviceModel
import com.olp.weco.model.PlantInfoResultModel
import com.olp.weco.model.PlantModel
import com.olp.weco.model.WeatherModel
import com.olp.weco.service.http.ApiPath
import com.olp.weco.view.DateType
import com.olp.lib.service.http.HttpCallback
import com.olp.lib.service.http.HttpErrorModel
import com.olp.lib.service.http.HttpResult
import kotlinx.coroutines.launch
import java.util.*

/**
 * 电站详情
 */
class PlantInfoViewModel : BaseViewModel() {

    var plantId: String? = null
    var plantModels: Array<PlantModel>? = null

    //图表数据请求参数
    var typeAndSn: Pair<Int, String>? = null
    var selectDate = Date()
    val chartTypes = PlantInfoResultModel.createChartType()
    var dataType = chartTypes[0]
    var dateType = DateType.HOUR //dateType 1 —— 时；2 —— 日；3 —— 月；4 —— 年

    val getPlantInfoLiveData = MutableLiveData<Pair<PlantModel?, String?>>()

    val getPlantWeatherInfoLiveData = MutableLiveData<Pair<WeatherModel?, String?>>()

    val getDeviceListLiveData = MutableLiveData<Pair<List<DeviceModel>, String?>>()

    /**
     * 获取电站详情
     */
    fun getPlantInfo() {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("plantId", plantId ?: "")
            }
            apiService().postForm(ApiPath.Plant.GET_PLANT_INFO, params, object :
                HttpCallback<HttpResult<PlantInfoResultModel>>() {
                override fun success(result: HttpResult<PlantInfoResultModel>) {
                    if (result.isBusinessSuccess()) {
                        getPlantInfoLiveData.value = Pair(result.obj?.plantBean, null)
                        getPlantWeatherInfoLiveData.value = Pair(result.obj?.weatherMap, null)
                    } else {
                        getPlantInfoLiveData.value = Pair(null, result.msg ?: "")
                        getPlantWeatherInfoLiveData.value = Pair(null, result.msg ?: "")
                    }
                }

                override fun onFailure(errorModel: HttpErrorModel) {
                    getPlantInfoLiveData.value = Pair(null, errorModel.errorMsg ?: "")
                    getPlantWeatherInfoLiveData.value = Pair(null, errorModel.errorMsg ?: "")
                }

            })
        }
    }




}
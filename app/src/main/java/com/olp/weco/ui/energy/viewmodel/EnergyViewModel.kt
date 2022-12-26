package com.olp.weco.ui.energy.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.olp.lib.service.http.HttpCallback
import com.olp.lib.service.http.HttpErrorModel
import com.olp.lib.service.http.HttpResult
import com.olp.weco.base.BaseViewModel
import com.olp.weco.model.energy.ChartModel
import com.olp.weco.service.http.ApiPath
import com.olp.weco.ui.chart.ChartListDataModel
import com.olp.weco.ui.chart.ChartYDataList
import com.olp.weco.ui.common.model.DataType
import kotlinx.coroutines.launch

class EnergyViewModel : BaseViewModel() {

    val stationLiveData = MutableLiveData<Pair<Boolean, ChartModel?>>()
    val chartLiveData = MutableLiveData<ChartListDataModel>()

    var dateType = DataType.TOTAL

    private val chartApi = listOf(
        ApiPath.Plant.GET_INVERTER_DATATOTAL,
        ApiPath.Plant.GET_INVERTER_DATAYEAR,
        ApiPath.Plant.GET_INVERTER_DATA_MONTH,
        ApiPath.Plant.GET_INVERTER_DATA_DAY
    )




    var time: String = ""

    var stationId = ""


    /**
     * 获取电站数据
     */
    fun getPlantChartData() {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("stationId", stationId)
                put("time", time)
            }
            apiService().postForm(
                chartApi[dateType],
                params,
                object : HttpCallback<HttpResult<ChartModel>>() {
                    override fun success(result: HttpResult<ChartModel>) {
                        val chartModel = result.obj

                        chartModel?.let {
                            val loadList = it.loadList
                            val timeList = mutableListOf<String>()
                            for (i in loadList.indices) {
                                timeList.add(i.toString())
                            }

                            val dataList = mutableListOf(
                                ChartYDataList(it.batList, "bat"),
                                ChartYDataList(it.gridList, "grid"),
                                ChartYDataList(it.solarList, "solar"),
                                ChartYDataList(it.loadList, "load"),
                            )

                            val chartListDataModel =
                                ChartListDataModel(timeList.toTypedArray(), dataList.toTypedArray())
                            chartLiveData.value = chartListDataModel

                        }
                        stationLiveData.value = Pair(result.isBusinessSuccess(), result.obj)
                    }

                    override fun onFailure(errorModel: HttpErrorModel) {
                        stationLiveData.value = Pair(false, null)
                    }
                })
        }
    }


}
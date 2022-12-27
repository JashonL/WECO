package com.olp.weco.ui.energy.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.olp.lib.service.http.HttpCallback
import com.olp.lib.service.http.HttpErrorModel
import com.olp.lib.service.http.HttpResult
import com.olp.lib.util.DateUtils
import com.olp.weco.base.BaseViewModel
import com.olp.weco.model.ImpactModel
import com.olp.weco.model.PlantModel
import com.olp.weco.model.energy.ChartModel
import com.olp.weco.service.http.ApiPath
import com.olp.weco.ui.chart.ChartListDataModel
import com.olp.weco.ui.chart.ChartYDataList
import com.olp.weco.ui.common.model.DataType
import com.olp.weco.ui.energy.ChartType
import kotlinx.coroutines.launch
import java.util.*

class EnergyViewModel : BaseViewModel() {



    val stationLiveData = MutableLiveData<Pair<Boolean, ChartModel?>>()

    val chartLiveData = MutableLiveData<ChartListDataModel>()



    var currentPlantId = ""

    var dateType = DataType.TOTAL

    var energyType = ChartType.HOME


    private val chartApi = listOf(
        ApiPath.Plant.GET_INVERTER_DATATOTAL,
        ApiPath.Plant.GET_INVERTER_DATAYEAR,
        ApiPath.Plant.GET_INVERTER_DATA_MONTH,
        ApiPath.Plant.GET_INVERTER_DATA_DAY
    )


    var time: String = ""


    fun setTime(date: Date) {
        time = when (dateType) {
            DataType.DAY -> DateUtils.yyyy_MM_dd_format(date)
            DataType.MONTH -> DateUtils.yyyy_MM_format(date)
            DataType.YEAR -> DateUtils.yyyy_format(date)
            else -> {
                DateUtils.yyyy_format(date)
            }
        }
    }


    /**
     * 获取电站数据
     */
    fun getPlantChartData() {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("plantId", currentPlantId)
                put("type", energyType.toString())
                put("date", time)
            }
            apiService().postForm(
                chartApi[dateType],
                params,
                object : HttpCallback<HttpResult<ChartModel>>() {
                    override fun success(result: HttpResult<ChartModel>) {
                        val chartModel = result.obj

                        chartModel?.let {
                            val loadList = it.energy
                            val timeList = mutableListOf<String>()
                            for (i in loadList.indices) {
                                timeList.add(i.toString())
                            }

                            val dataList = mutableListOf(
                                ChartYDataList(it.energy, ChartType.getNameByType(energyType)),
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
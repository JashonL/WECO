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
import com.olp.weco.model.energy.DayChartModel
import com.olp.weco.service.http.ApiPath
import com.olp.weco.ui.chart.ChartListDataModel
import com.olp.weco.ui.chart.ChartYDataList
import com.olp.weco.ui.common.model.DataType
import com.olp.weco.ui.energy.ChartType
import kotlinx.coroutines.launch
import java.util.*

class EnergyViewModel : BaseViewModel() {


    val stationLiveData = MutableLiveData<Pair<Boolean, ChartModel?>>()
    val stationDayLiveData = MutableLiveData<Pair<Boolean, DayChartModel?>>()

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
        time = DateUtils.yyyy_MM_dd_format(date)
    }


    /**
     * 获取电站数据  总年月
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
                            val loadList = if (dateType == DataType.DAY) {
                                it.power
                            } else {
                                it.energy
                            }


                            val timeList = mutableListOf<String>()
                            for (i in loadList.indices) {
                                timeList.add(i.toString())
                            }

                            val dataList = if (dateType == DataType.DAY) {
                                mutableListOf(
                                    ChartYDataList(
                                        it.power,
                                        ChartType.getNameByType(energyType)
                                    )
                                )
                            } else {
                                mutableListOf(
                                    ChartYDataList(
                                        it.energy,
                                        ChartType.getNameByType(energyType)
                                    )
                                )
                            }

                            val chartListDataModel =
                                ChartListDataModel(timeList.toTypedArray(), dataList.toTypedArray())
                            chartLiveData.value = chartListDataModel

                        }
                        stationLiveData.value = Pair(result.isBusinessSuccess(), chartModel)
                    }

                    override fun onFailure(errorModel: HttpErrorModel) {
                        stationLiveData.value = Pair(false, null)
                    }
                })
        }
    }


    /**
     * 获取电站数据  日
     */
    fun getDayChartData() {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("plantId", currentPlantId)
                put("type", energyType.toString())
                put("date", time)
            }
            apiService().postForm(
                chartApi[dateType],
                params,
                object : HttpCallback<HttpResult<DayChartModel>>() {
                    override fun success(result: HttpResult<DayChartModel>) {
                        val chartModel = result.obj

                        chartModel?.let {
                            val loadList = it.power


                            val timeList = mutableListOf<String>()
                            for (i in loadList.indices) {
                                timeList.add(i.toString())
                            }

                            val dataList =   mutableListOf(
                                ChartYDataList(
                                    it.power,
                                    ChartType.getNameByType(energyType)
                                )
                            )

                            val chartListDataModel =
                                ChartListDataModel(timeList.toTypedArray(), dataList.toTypedArray())
                            chartLiveData.value = chartListDataModel

                        }
                        stationDayLiveData.value = Pair(result.isBusinessSuccess(), chartModel)
                    }

                    override fun onFailure(errorModel: HttpErrorModel) {
                        stationDayLiveData.value = Pair(false, null)
                    }
                })
        }
    }


}
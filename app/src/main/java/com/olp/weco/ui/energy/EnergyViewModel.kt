package com.olp.weco.ui.energy

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.olp.weco.base.BaseViewModel
import com.olp.weco.model.ImpactModel
import com.olp.weco.model.PlantModel
import com.olp.weco.model.energy.ChartModel
import com.olp.weco.service.http.ApiPath
import com.olp.weco.ui.chart.ChartListDataModel
import com.olp.weco.ui.chart.ChartYDataList
import com.olp.weco.ui.common.model.DataType
import com.olp.lib.service.http.HttpCallback
import com.olp.lib.service.http.HttpErrorModel
import com.olp.lib.service.http.HttpResult
import com.olp.lib.util.DateUtils
import kotlinx.coroutines.launch
import java.util.Date

class EnergyViewModel : BaseViewModel() {

    val impactLiveData = MutableLiveData<ImpactModel?>()

    val stationLiveData = MutableLiveData<Pair<Boolean, ChartModel?>>()

    val chartLiveData = MutableLiveData<ChartListDataModel>()

    val impactChartLiveData = MutableLiveData<ChartListDataModel>()


    var currentStation: PlantModel? = null

    var dateType = DataType.TOTAL

    private val chartApi = listOf(
        ApiPath.Plant.GET_INVERTER_DATATOTAL,
        ApiPath.Plant.GET_INVERTER_DATAYEAR,
        ApiPath.Plant.GET_INVERTER_DATA_MONTH,
        ApiPath.Plant.GET_INVERTER_DATA_DAY
    )

    private val impactApi = listOf(
        ApiPath.Plant.GET_IMPACT_TOTAL,
        ApiPath.Plant.GET_IMPACT_YEAR,
        ApiPath.Plant.GET_IMPACT_MONTH,
        ApiPath.Plant.GET_IMPACT_DAY
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
                put("stationId", currentStation?.id.toString())
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


    /**
     * 获取电站数据
     */
    fun getPlantImpactData() {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("stationId", currentStation?.id.toString())
                put("time", time)
            }
            apiService().postForm(
                impactApi[dateType],
                params,
                object : HttpCallback<HttpResult<ImpactModel>>() {
                    override fun success(result: HttpResult<ImpactModel>) {
                        val impactModel = result.obj

                        impactModel?.let {
                            val loadList = it.impactList
                            val timeList = mutableListOf<String>()
                            if (loadList == null) return
                            for (i in loadList.indices) {
                                timeList.add(i.toString())
                            }

                            val dataList = mutableListOf(
                                ChartYDataList(it.impactList, "impact"),
                            )

                            val chartListDataModel =
                                ChartListDataModel(timeList.toTypedArray(), dataList.toTypedArray())
                            impactLiveData.value = impactModel
                            impactChartLiveData.value = chartListDataModel
                        }
                    }

                    override fun onFailure(errorModel: HttpErrorModel) {
                        impactLiveData.value = null
                    }
                })
        }
    }


}
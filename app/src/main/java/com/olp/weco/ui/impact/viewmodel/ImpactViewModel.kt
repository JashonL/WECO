package com.olp.weco.ui.impact.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.olp.lib.service.http.HttpCallback
import com.olp.lib.service.http.HttpErrorModel
import com.olp.lib.service.http.HttpResult
import com.olp.lib.util.DateUtils
import com.olp.weco.base.BaseViewModel
import com.olp.weco.model.ImpactModel
import com.olp.weco.model.energy.ChartModel
import com.olp.weco.service.http.ApiPath
import com.olp.weco.ui.chart.ChartListDataModel
import com.olp.weco.ui.chart.ChartYDataList
import com.olp.weco.ui.common.model.DataType
import com.olp.weco.ui.energy.ChartType
import kotlinx.coroutines.launch
import java.util.*

class ImpactViewModel : BaseViewModel() {

    val impactChartLiveData = MutableLiveData<ChartListDataModel>()

    val impactLiveData = MutableLiveData<ImpactModel?>()

    var currentPlantId = ""


    private val impactApi = listOf(
        ApiPath.Plant.GET_IMPACT_YEAR,
        ApiPath.Plant.GET_IMPACT_MONTH,
        ApiPath.Plant.GET_IMPACT_DAY
    )

    var dateType = DataType.YEAR


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
    fun getPlantImpactData() {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("plantId", currentPlantId)
            }
            apiService().postForm(
                impactApi[dateType],
                params,
                object : HttpCallback<HttpResult<ImpactModel>>() {
                    override fun success(result: HttpResult<ImpactModel>) {
                        val impactModel = result.obj

                        impactModel?.let {
                            val loadList = it.saveCosts
                            val timeList = mutableListOf<String>()
                            for (i in loadList.indices) {
                                timeList.add(i.toString())
                            }

                            val dataList = mutableListOf(
                                ChartYDataList(it.saveCosts, "impact"),
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
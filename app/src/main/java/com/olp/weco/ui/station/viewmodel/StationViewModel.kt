package com.olp.weco.ui.station.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.olp.weco.base.BaseViewModel
import com.olp.weco.model.PlantModel
import com.olp.weco.service.http.ApiPath
import com.olp.lib.service.http.HttpCallback
import com.olp.lib.service.http.HttpErrorModel
import com.olp.lib.service.http.HttpResult
import kotlinx.coroutines.launch

class StationViewModel : BaseViewModel() {

    val getPlantListLiveData = MutableLiveData<Pair<Boolean, Array<PlantModel>?>>()

    var currentStation: PlantModel? = null




    /**
     * 获取电站列表
     */
    fun getPlantList() {
        val email = accountService().user()?.email
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("email", email.toString())
            }
            apiService().postForm(
                ApiPath.Plant.STATIONLIST,
                params,
                object : HttpCallback<HttpResult<Array<PlantModel>>>() {
                    override fun success(result: HttpResult<Array<PlantModel>>) {
                        getPlantListLiveData.value = Pair(result.isBusinessSuccess(), result.obj)
                    }

                    override fun onFailure(errorModel: HttpErrorModel) {
                        getPlantListLiveData.value = Pair(false, null)
                    }
                })
        }
    }





}
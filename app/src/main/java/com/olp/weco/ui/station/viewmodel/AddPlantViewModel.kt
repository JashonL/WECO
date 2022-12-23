package com.olp.weco.ui.station.viewmodel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.olp.weco.R
import com.olp.weco.app.WECOApplication
import com.olp.weco.base.BaseViewModel
import com.olp.weco.model.*
import com.olp.weco.service.http.ApiPath
import com.olp.lib.service.http.HttpCallback
import com.olp.lib.service.http.HttpErrorModel
import com.olp.lib.service.http.HttpResult
import kotlinx.coroutines.launch
import java.io.File

/**
 * 添加电站
 */
class AddPlantViewModel : BaseViewModel() {

    var isEditMode: Boolean = false

    var addPlantModel = AddPlantModel()

    val timeZoneLiveData = MutableLiveData<Pair<TimeZone?, String?>>()

    val addPlantLiveData = MutableLiveData<Pair<String?, String?>>()

    val deletePlantLiveData = MutableLiveData<Pair<Boolean, String?>>()


    val editPlantLiveData = MutableLiveData<String?>()

    val cityListLiveData = MutableLiveData<Pair<Array<CityModel>?, String?>>()

    val currencyListLiveData = MutableLiveData<Pair<Array<GeneralItemModel>, String?>>()

    val moneyUtilListLiveData = MutableLiveData<Array<CurrencyModel>?>()


    /**
     * 获取时区列表
     */
    fun fetchTimeZoneList(country: String) {
        viewModelScope.launch {
            apiService().postForm(
                ApiPath.Plant.GET_TIMEZONE_BY_COUNTRY,
                hashMapOf(Pair("country", country)),
                object : HttpCallback<HttpResult<TimeZone>>() {
                    override fun success(result: HttpResult<TimeZone>) {
                        if (result.isBusinessSuccess()) {
                            timeZoneLiveData.value = Pair(result.obj, null)
                        } else {
                            timeZoneLiveData.value = Pair(null, result.msg ?: "")
                        }
                    }

                    override fun onFailure(errorModel: HttpErrorModel) {
                        timeZoneLiveData.value = Pair(null, errorModel.errorMsg ?: "")
                    }

                })
        }
    }

    /**
     * 获取货币列表
     */
    fun fetchCurrencyList() {
        val map = HashMap<String, String>().apply {
            put("email", accountService().user()?.email ?: "")
        }
        viewModelScope.launch {
            apiService().postForm(
                ApiPath.Plant.CURRENCY_LIST,
                map,
                object : HttpCallback<HttpResult<Array<CurrencyModel>>>() {
                    override fun success(result: HttpResult<Array<CurrencyModel>>) {
                        if (result.isBusinessSuccess()) {
                            moneyUtilListLiveData.value = result.obj
                        } else {
                            moneyUtilListLiveData.value = null
                        }
                    }

                    override fun onFailure(errorModel: HttpErrorModel) {
                        moneyUtilListLiveData.value = null
                    }

                })
        }
    }

    /**
     * 获取城市列表
     */
    fun fetchCityList(country: String) {
        viewModelScope.launch {
            apiService().postForm(
                ApiPath.Plant.GET_CITY_LIST,
                hashMapOf(Pair("country", country)),
                object : HttpCallback<HttpResult<Array<CityModel>>>() {
                    override fun success(result: HttpResult<Array<CityModel>>) {
                        if (result.isBusinessSuccess()) {
                            val citylist = result.obj
                            if (citylist == null) {
                                cityListLiveData.value = Pair(emptyArray(), null)
                            } else {
                                cityListLiveData.value = Pair(citylist, null)
                            }
                        } else {
                            cityListLiveData.value = Pair(emptyArray(), result.msg ?: "")
                        }
                    }

                    override fun onFailure(errorModel: HttpErrorModel) {
                        cityListLiveData.value = Pair(emptyArray(), errorModel.errorMsg ?: "")
                    }

                })
        }
    }

    /**
     * 新增电站
     */
    fun addPlant() {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("stationType", addPlantModel.stationType!!)
                put("stationName", addPlantModel.plantName!!)
                put("installtionDate", addPlantModel.getDateString())
                put("country", addPlantModel.country!!)

                put("city", addPlantModel.city ?: "")
                put("address", addPlantModel.address!!)
                put("incomeUnit", addPlantModel.formulaMoneyUnitId ?: "daller")
            }

            apiService().postFile(
                ApiPath.Plant.ADD_PLANT,
                params,
                if (TextUtils.isEmpty(addPlantModel.plantFileCompress)) null else File(addPlantModel.plantFileCompress!!),
                object : HttpCallback<HttpResult<AddPlantModel>>() {
                    override fun success(result: HttpResult<AddPlantModel>) {
                        if (result.isBusinessSuccess()) {
                            addPlantLiveData.value = Pair(result.obj?.plantID, null)
                        } else {
                            addPlantLiveData.value = Pair(null, result.msg ?: "")
                        }
                    }

                    override fun onFailure(errorModel: HttpErrorModel) {
                        addPlantLiveData.value = Pair(null, errorModel.errorMsg ?: "")
                    }

                })
        }
    }

    /**
     * 修改电站
     */
    fun editPlant() {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("ID", addPlantModel.plantID!!)
                put("plantName", addPlantModel.plantName!!)
                put("installDate", addPlantModel.getDateString())
                put("country", addPlantModel.country!!)
                put("city", addPlantModel.city!!)
                addPlantModel.plantAddress?.also {
                    put("plantAddress", it)
                }
                addPlantModel.plant_lat?.also {
                    put("plant_lat", it.toString())
                }
                addPlantModel.plant_lng?.also {
                    put("plant_lng", it.toString())
                }
                put("plantTimeZone", addPlantModel.plantTimeZone ?: "")
                put("nominalPower", addPlantModel.totalPower!!)
                put("formulaMoney", addPlantModel.formulaMoney ?: "0")
                put("formulaMoneyUnitId", addPlantModel.formulaMoneyUnitId ?: "")
            }
            apiService().postFile(
                ApiPath.Plant.UPDATE_PLANT,
                params,
                if (TextUtils.isEmpty(addPlantModel.plantFileCompress)) null else File(addPlantModel.plantFileCompress!!),
                object : HttpCallback<HttpResult<AddPlantModel>>() {
                    override fun success(result: HttpResult<AddPlantModel>) {
                        if (result.isBusinessSuccess()) {
                            editPlantLiveData.value = null
                        } else {
                            editPlantLiveData.value = result.msg ?: ""
                        }
                    }

                    override fun onFailure(errorModel: HttpErrorModel) {
                        editPlantLiveData.value = errorModel.errorMsg ?: ""
                    }

                })
        }
    }


    /**
     * 删除电站
     */
    fun deletePlant(stationId: String) {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("stationId", addPlantModel.plantID!!)
            }
            apiService().postForm(
                ApiPath.Plant.DELETE_PLANT,
                params,
                object : HttpCallback<HttpResult<AddPlantModel>>() {
                    override fun success(result: HttpResult<AddPlantModel>) {
                        if (result.isBusinessSuccess()) {
                            deletePlantLiveData.value = Pair(
                                true, WECOApplication.instance().getString(
                                    R.string.m131_set_success
                                )
                            )
                        } else {
                            deletePlantLiveData.value = Pair(false, result.msg)
                        }
                    }

                    override fun onFailure(errorModel: HttpErrorModel) {
                        deletePlantLiveData.value = Pair(false, errorModel.errorMsg ?: "")
                    }

                })
        }
    }


}
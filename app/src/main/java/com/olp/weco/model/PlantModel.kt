package com.olp.weco.model

import android.os.Parcelable
import com.olp.weco.utils.ValueUtil
import com.olp.lib.util.DateUtils
import com.olp.lib.util.Util
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlantModel @JvmOverloads constructor(
    val id: String? = null,//电站ID
    val plantName: String? = null,//电站名
    val plantImgName: String? = null,//电站图片
    val createDateText: String? = null,//安装日期-2022-03-03
    val hasDeviceOnLine: Int = 0,//电站状态   0：离线  1：故障  2：在线
    val city: String? = null,//城市
    val atsTimezoneStr: String? = null,//时区
    val currentPacStr: String? = null,//实时功率0kW
    val nominalPowerStr: String? = null,//组件总功率1130kWp
    var nominalPower: String? = null,//组件总功率不带单位的，1130000
    val eToday: Double? = null,//今日发电量
    val energyMonth: Double? = null,//月发电量
    val eTotal: Double? = null,//累计发电量
    val country: String? = null,//国家
    val plantAddress: String? = null,//详细地址
    var plant_lat: String? = null,//纬度
    var plant_lng: String? = null,//经度
    var formulaMoneyUnitId: String? = null,//货币单位
    var formulaMoney: String? = null,//资金收益
    var atsDeviceFlag: Int = 1,// 是否有设备 0 无设备； 1 有设备
    val stationType: String,
    val stationName: String,
    val installtionDate: String,
    val address: String,
    val incomeUnit: String,
    val pictureAddress: String,
    val userId: String,
    val onlineStatus: Int,
    val currencyPower: String,
    val pvcapacity: String,
    val today: String?,
    val total: String?

) : Parcelable {

    companion object {
        /**
         * 类型-全部
         */
        const val PLANT_STATUS_ALL = -1

        /**
         * 类型-离线
         */
        const val PLANT_STATUS_OFFLINE = 0

        /**
         * 类型-故障
         */
        const val PLANT_STATUS_TROUBLE = 2

        /**
         * 类型-在线
         */
        const val PLANT_STATUS_ONLINE = 1




        //电站类型
        const val PLANT_PV="PV"
        const val PLANT_STORAGE="storage"

    }

    fun convert(): AddPlantModel {
        val addPlantModel = AddPlantModel()
        addPlantModel.plantID = id
        addPlantModel.plantName = plantName
        addPlantModel.plantFileService = plantImgName
        addPlantModel.installDate = DateUtils.from_yyyy_MM_dd_format(createDateText ?: "")
        addPlantModel.country = country
        addPlantModel.city = city
        addPlantModel.plantAddress = plantAddress
        addPlantModel.plantTimeZone = atsTimezoneStr
        addPlantModel.totalPower = nominalPower
        addPlantModel.formulaMoney = formulaMoney
        addPlantModel.formulaMoneyUnitId = formulaMoneyUnitId
        return addPlantModel
    }

    fun getETodayText(): String {
        return Util.getDoubleText(today?.toDouble())
    }

    fun getETodayWithUnitText(): String {
        val valueFromKWh = ValueUtil.valueFromKWh(eToday)
        return valueFromKWh.first + valueFromKWh.second
    }

    fun getETotalText(): String {
        return Util.getDoubleText(total?.toDouble())
    }

    fun getETotalWithUnitText(): String {
        val valueFromKWh = ValueUtil.valueFromKWh(eTotal)
        return valueFromKWh.first + valueFromKWh.second
    }

    /**
     * 当月发电
     */
    fun getMonthGenerateElectricity(): String {
        return Util.getDoubleText(energyMonth)
    }

    fun getMonthGenerateElectricityWithUnitText(): String {
        val valueFromKWh = ValueUtil.valueFromKWh(energyMonth)
        return valueFromKWh.first + valueFromKWh.second
    }

    fun hasDevices(): Boolean {
        return atsDeviceFlag == 1
    }
}


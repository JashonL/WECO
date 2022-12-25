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
    val plantType: String? = null,//电站名
    val installationData: String? = null,//电站名
    val country: String? = null,//电站名
    val city: String? = null,//电站名
    val address: String? = null,//电站名
    val incomeUnit: String? = null,//电站名
    val plantPicturePath: String? = null,//电站名
    val currencyPower: String? = null,//电站名
    val pvCapacity: Double? = null,//电站名
    val totalCharge: Double? = null,//电站名
    val todayCharge: Double? = null,//电站名
    val plantStatus: Int? = null,//电站名
    val plantPicturePathText: String? = null,//电站名


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
        addPlantModel.country = country
        addPlantModel.city = city
        return addPlantModel
    }



}


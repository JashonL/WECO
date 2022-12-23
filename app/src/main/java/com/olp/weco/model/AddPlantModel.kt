package com.olp.weco.model

import android.net.Uri
import com.olp.lib.util.DateUtils
import java.util.*

/**
 * 添加电站
 */
class AddPlantModel {

    //电站名
    var plantName: String? = null

    //电站ID
    var plantID: String? = null

    //安装日期
    var installDate: Date? = null

    //国家
    var country: String? = null

    //城市
    var city: String? = null

    //详细地址
    var plantAddress: String? = null

    //纬度
    var plant_lat: String? = null

    //经度
    var plant_lng: String? = null

    //总功率
    var totalPower: String? = null

    //时区
    var plantTimeZone: String? = null

    //收益公式
    var formulaMoney: String? = "1.2"

    //货币单位
    var formulaMoneyUnitId: String? = null

    //电站图片(本地图片)
    var plantFile: Uri? = null

    //压缩后的电站图片（本地图片）
    var plantFileCompress: String? = null

    //电站图片（服务器图片，修改电站信息的时候会使用到）
    var plantFileService: String? = null

    //电站类型
    var stationType:String?=null


    //电站地址
    var address:String?=null

    fun getDateString(): String {
        if (installDate == null) {
            installDate = Date()
        }
        return DateUtils.yyyy_MM_dd_format(installDate!!)
    }

}
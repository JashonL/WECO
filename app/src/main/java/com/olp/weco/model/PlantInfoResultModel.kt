package com.olp.weco.model

import com.olp.weco.R
import com.olp.weco.app.WECOApplication


/**
 * 电站详情服务端返回来Model
 */
data class PlantInfoResultModel(val plantBean: PlantModel, val weatherMap: WeatherModel) {

    companion object {
        /**
         * 创建图表类型(传给服务端的类型)
         *
         * 1 —— 功率/电量
         * 2 —— SOC
         * 3 —— 充放电
         */
        fun createChartType(): Array<ChartTypeModel> {
            return arrayOf(
                ChartTypeModel(
                    "1",
                    WECOApplication.instance().getString(R.string.m61_power_output),
                    WECOApplication.instance().getString(R.string.m48_kwh)
                ),
                ChartTypeModel(
                    "2",
                    WECOApplication.instance().getString(R.string.m62_soc),
                    "%"
                ),
                ChartTypeModel(
                    "3",
                    WECOApplication.instance().getString(R.string.m63_charge_discharge_output),
                    WECOApplication.instance().getString(R.string.m48_kwh)
                )
            )
        }
    }

}


data class WeatherModel(
    val success: Boolean,//天气是否获取成功
    val icon: String?,//天气图标
    val newTmp: String?,//温度-32.0°C
    val city: String?,//城市
    val cond_txt: String?,//晴
    val wind_dir: String?,//风向-北风
    val wind_spd: String?,// 风速-20km/h
    val sr: String?,//日出-06:03
    val ss: String?,//日落-18:10
    val duration: String?//日照时长-12h07min
) {
    fun getCityAndWeatherText(): String {
        return "$city | $cond_txt"
    }

    fun getWindDirectionText(): String {
        return WECOApplication.instance().getString(R.string.m64_wind_direction_format, wind_dir)
    }

    fun getWindSpeedText(): String {
        return WECOApplication.instance().getString(R.string.m65_wind_speed_format, wind_spd)
    }
}

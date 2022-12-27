package com.olp.weco.service.http

object ApiPath {

    object Mine {
        /**
         * 登录
         */

        const val LOGIN = "v1/user/login"

        /**
         * 登出
         */
        const val LOGOUT = "v1/user/logOut"

        /**
         * 注册
         */
        const val REGISTER = "v1/user/register"


        /**
         * 设置-修改密码
         */
        const val MODIFY_PASSWORD = "/v1/user/modifyPassword"

        /**
         * 修改用户信息
         */
        const val MODIFYUSERINFO = "/v1/user/modifyUserInfo"

        /**
         * 找回密码
         */
        const val FINDPASSWORD = "/v1/user/findPassword"

        /**
         * 获取邮箱验证码
         */
        const val SENDEMAILCODE = "/v1/user/sendEmailCode"

        /**
         * 上传用户头像
         */
        const val UPLOADAVATAR = "/v1/user/uploadAvatar"

        /**
         * 忘记密码
         */
        const val RETRIEVEPASSWORD = "v1/user/retrievePassword"


    }

    object Plant {

        const val STATIONLIST = "v1/plant/getPlantList"


        /**
         * 请求储能机状态
         */
        const val GETDATAOVERVIEW = "v1/plant/getPlantOverviewData"


        /**
         * 货币列表
         */
        const val CURRENCY_LIST = "v1/station/getIncomeUnit"

        /**
         * 根据国家来获取时区列表
         */
        const val GET_TIMEZONE_BY_COUNTRY = "ATSregister/getTimezoneByCountry"

        /**
         * 城市列表
         */
        const val GET_CITY_LIST = "v1/user/getCountryCityList"


        /**
         * 添加电站
         */
        const val ADD_PLANT = "v1/station/addStation"

        /**
         * 修改电站
         */
        const val UPDATE_PLANT = "ATSplant/updatePlant"


        /**
         * 删除电站
         */
        const val DELETE_PLANT = "v1/station/deleteStation"

        /**
         * 获取电站详情
         */
        const val GET_PLANT_INFO = "ATSplant/getUserCenterEnertyDataByPlantid"

        /**
         * 根据日期类型请求图表
         */
        const val GET_INVERTER_DATA_DAY = "v1/plant/getEnergyHour"
        const val GET_INVERTER_DATA_MONTH = "v1/plant/getEnergyDay"
        const val GET_INVERTER_DATAYEAR = "v1/plant/getEnergyMonth"
        const val GET_INVERTER_DATATOTAL = "v1/plant/getEnergyYear"


        /**
         * 根据日期类型请求收益
         */
        const val GET_IMPACT_DAY = "v1/plant/getEnergyImpactDay"
        const val GET_IMPACT_MONTH = "v1/plant/getEnergyImpactMonth"
        const val GET_IMPACT_YEAR = "v1/plant/getEnergyImpactYear"


    }


    object Commom {
        /**
         * 服务-使用手册列表
         */
        const val GETCOUNTRYLIST = "v1/user/getCountryList"
    }


    object Service {
        /**
         * 服务-使用手册列表
         */
        const val GET_SERVICE_MANUAL = "ATService/getManual"

        /**
         * 服务-安装视频列表
         */
        const val GET_INSTALL_VIDEO = "ATService/getInstallVideo"
    }


    object Dataloger {

        /**
         * 添加采集器
         */
        const val ADDDATALOG = "v1/station/addDatalog"

    }

}
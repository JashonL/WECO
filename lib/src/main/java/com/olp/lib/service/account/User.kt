package com.olp.lib.service.account

/**
 * 用户ID
 * 用户名
 * 手机号码
 * 安装商编号
 * 邮件
 * token
 */
data class User(
    val id: String,
    val country: String,
    val timeZone: String,
    val email: String,
    val installerCode: String,
    val username:String,
    val phone:String,
    val appVer:String,
    val userType: String,
    val phoneOSType: String,
    val phoneModel: String,
    val appVersion: String,
    val avatarAddress: String,
    val lastLoginIp: String,
    val createTime: String,
    val delTimeStrap: String,
    val createTimeStrap: String,
    val picAddressText: String,
    var password: String
)


/*
*
* {"id":20,"country":
* "China","timeZone":8.0,
* "email":"721695214@qq.com",
* "installerCode":null,"type":0,
* "username":null,
* "password":"E10ADC3949BA59ABBE56E057F20F883E",
* "phone":null,"phoneOSType":null,"appVer":null,
* "phoneMode":null,"lastLoginIp":"119.128.113.226",
* "createTime":"2022-12-24 21:04:16","lastLoginTime":"2022-12-24 21:15:12",
* "picAddress":null,"delTimeStrap":0,"createTimeStrap":1671887056066,"picAddressText":"https://cdn.shuoxd.com/null"}}
*
* */

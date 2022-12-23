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
    val userType: String,
    val email: String,
    val phoneOs: String,
    val phoneModel: String,
    val appVersion: String,
    val country: String,
    val avatarAddress: String,
    val timeZone: String,
    val installerCode: String,
    var password:String
)




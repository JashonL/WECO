package com.olp.weco.model

import com.olp.lib.service.account.User
import com.olp.lib.service.appinfo.AppSystemDto

data class LoginModel(
    val user: User,
    val appSystemDto: AppSystemDto
)
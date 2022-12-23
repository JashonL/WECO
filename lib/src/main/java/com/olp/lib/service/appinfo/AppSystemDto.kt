package com.olp.lib.service.appinfo

data class AppSystemDto(
    val phoneOs: String,
    val needUpgrade: String,
    val forcedUpgrade: String,
    val nowVersion: String,
    val nowVersionIsBeta: String,
    val lastVersion: String,
    val lastVersionUpgradeUrl: String,
    val lastVersionIsBeta: String,
    val lastVersionUpgradeDescription: String
 )


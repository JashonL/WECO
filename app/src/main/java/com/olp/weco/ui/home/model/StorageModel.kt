package com.olp.weco.ui.home.model

data class StorageModel(
    val overviewType: String,
    val solar: String?,
    val grid: String?,
    val load: String?,
    val bat: String?,
    val home: String?,
    val inverterNums: String?,
    val onlineStatus: String?
)


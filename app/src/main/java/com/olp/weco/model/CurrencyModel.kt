package com.olp.weco.model

import com.olp.weco.view.dialog.ItemName

data class CurrencyModel(
    val id: Int,
    val incomeUnit: String
) : ItemName {
    override fun itemName(): String {
        return incomeUnit
    }
}
package com.olp.weco.model

import com.olp.weco.view.dialog.ItemName


/**
 * 通用选择器器Model
 */
data class GeneralItemModel(val name: String) : ItemName {

    companion object {
        fun convert(names: Array<String>): Array<GeneralItemModel> {
            return Array(names.size) { index -> GeneralItemModel(names[index]) }
        }
    }

    override fun itemName(): String {
        return name
    }
}
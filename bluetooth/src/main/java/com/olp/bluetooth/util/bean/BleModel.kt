package com.olp.bluetooth.util.bean

data class BleModel(
    val name: String?,
    val address: String?
){


    override fun toString(): String {
        return "name:"+name+"address:"+address
    }
}
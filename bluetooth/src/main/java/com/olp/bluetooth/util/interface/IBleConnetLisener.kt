package com.olp.bluetooth.util.`interface`


interface IBleConnetLisener {

    fun connectError()

    fun connectSuccess()


    fun responData(receviceData: ByteArray?)

}
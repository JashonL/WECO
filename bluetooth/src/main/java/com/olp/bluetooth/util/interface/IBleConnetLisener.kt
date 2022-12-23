package com.ttech.bluetooth.util.`interface`

import com.ttech.bluetooth.util.bean.BleModel

interface IBleConnetLisener {

    fun connectError()

    fun connectSuccess()


    fun responData(receviceData: ByteArray?)

}
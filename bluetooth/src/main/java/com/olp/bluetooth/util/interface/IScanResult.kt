package com.ttech.bluetooth.util.`interface`

import com.ttech.bluetooth.util.bean.BleModel

interface IScanResult {
    fun scanning(model:BleModel)

    fun scanResult(results:List<BleModel>)

}
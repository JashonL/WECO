package com.olp.bluetooth.util.`interface`

import com.olp.bluetooth.util.bean.BleModel

interface IScanResult {
    fun scanning(model: BleModel)

    fun scanResult(results:List<BleModel>)

}
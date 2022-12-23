package com.ttech.bluetooth.util.`interface`

interface IBleConnect {

    /**
     * 蓝牙是否已经打开
     */
    fun isBleEnable():Boolean?



    /**
     * 打开蓝牙
     */

    fun openBle()

    /**
     *扫描蓝牙
     */
    fun scan(callback:IScanResult?)


    /**
     * 停止扫描
     */
    fun stopScan()



    /**
     * 连接蓝牙
     */
    fun connect(mac:String,callback: IBleConnetLisener?)


    /**
     * 设置MTU
     */

    fun setMtu(mtu:Int,callback:IBleConnetLisener?)


    /**
     * 发送数据
     */
    fun sendData(value:ByteArray)


    /**
     * 接收数据
     */

    fun receiveData(data: ByteArray?,callback:IBleConnetLisener?)

    /**
     * .断开蓝牙
     */
    fun disConnect()





}
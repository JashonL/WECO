package com.olp.weco.service.ble

object BleCommand {

    //wifi名称
    const val WIFI_SSID = 56

    //wifi密码
    const val WIFI_PASSWORD = 57

    //dhcp
    const val NET_DHCP = 71

    //路由器ip
    const val REMOTE_IP = 17

    //网关
    const val DEFAULT_GATEWAY = 26

    //掩码
    const val SUBNET_MASK = 25

    //时间间隔
    const val DATA_INTERVAL = 4

    //采集器时间
    const val SYSTEM_TIME = 31

    //服务器域名
    const val REMOTE_URL = 19

    //采集器IP
    const val LOCAL_IP = 14

    //服务器端口
    const val REMOTE_PORT = 18

    //采集器序列号
    const val DATALOGGER_SN = 8

    //MAC地址
    const val LOCAL_MAC = 16

    //采集器设备类型
    const val DATALOGGER_TYPE = 13

    //软件版本号
    const val FIRMWARE_VERSION = 21

    //升级的文件类型
    const val FOTA_FILE_TYPE = 65

    //采集器重启命令
    const val DATALOGGER_RESTART = 32

    //WIFI连接状态
    const val LINK_STATUS = 60

    //蓝牙密钥
    const val BLUETOOTH_KEY = 54

    //查询状态
    const val CHECKCONNET_STATUS = 55

    //设置文件类型
    const val UPDATA_FILE_TYPE = 80

    //查询采集器升级进度
    const val UPDATA_PROGRESS = 101



}
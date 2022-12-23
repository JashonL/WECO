package com.ttech.bluetooth.util.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.text.TextUtils
import java.util.*


object WifiUtils {
    /**
     * 获取SSID
     *
     * @param activity 上下文
     * @return WIFI 的SSID
     */
    fun getWifiSSid(activity: Activity): String? {
        var ssid = ""

        val manager = (activity.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)

        Objects.requireNonNull(manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI))?.getState() ?: return null

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
            val mWifiManager =
                (activity.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager)
            val info = mWifiManager.connectionInfo
            return info.ssid.replace("\"", "")
        } else {
            val mWifiManager = activity.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val connectionInfo = mWifiManager.connectionInfo
            val networkId = connectionInfo.networkId

            ssid = connectionInfo.ssid
            @SuppressLint("MissingPermission") val configuredNetworks =
                mWifiManager.configuredNetworks
            for (wificonf in configuredNetworks) {
                if (wificonf.networkId == networkId) {
                    ssid = wificonf.SSID
                    break
                }
            }
            if (TextUtils.isEmpty(ssid)) return ssid
            if (ssid.contains("\"")) {
                ssid = ssid.replace("\"", "")
            }
            if (ssid.lowercase(Locale.getDefault()).contains("unknown ssid")) {
                ssid = ""
            }
        }
        return ssid
    }


}
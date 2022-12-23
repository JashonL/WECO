package com.ttech.bluetooth.util.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings


object LocalUtils {


    //打开gps定位
    const val OPEN_GPS_CODE = 102

    /**
     * 检查GPS是否打开
     *
     */
    fun checkGPSIsOpen(activity: Activity): Boolean {
        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }


    /**
     * 去手机设置打开GPS
     *
     * @param activity
     */
    fun goToOpenGPS(activity: Activity) {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        activity.startActivityForResult(intent, OPEN_GPS_CODE)
    }
}
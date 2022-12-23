package com.olp.lib.util

import android.util.Log
import com.olp.lib.BuildConfig

object LogUtil {

    fun i(tag: String, log: String) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, log)
        }
    }
}
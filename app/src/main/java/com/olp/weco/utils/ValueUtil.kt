package com.olp.weco.utils

import com.olp.weco.R
import com.olp.weco.app.WECOApplication
import com.olp.lib.util.Util
import java.math.BigDecimal

/**
 * 数值换算工具，根据规则换算出单位
 */
object ValueUtil {
    /**
     * 电量单位：数值转换，基础单位是kWh
     */
    fun valueFromKWh(kwhValue: Double?): Pair<String, String> {
        return when {
            kwhValue == null -> {
                Pair("0", WECOApplication.instance().getString(R.string.m48_kwh))
            }

            kwhValue > 0 -> {
                if (kwhValue < 10000) {
                    Pair(
                        Util.getDoubleText(kwhValue),
                        WECOApplication.instance().getString(R.string.m48_kwh)
                    )
                } else {
                    Pair(
                        Util.getDoubleText(kwhValue / 1000),
                        WECOApplication.instance().getString(R.string.m49_mwh)
                    )
                }
            }


            kwhValue < 0 -> {
                if (kwhValue > -10000) {
                    Pair(
                        Util.getDoubleText(kwhValue),
                        WECOApplication.instance().getString(R.string.m48_kwh)
                    )
                } else {
                    Pair(
                        Util.getDoubleText(kwhValue / 1000),
                        WECOApplication.instance().getString(R.string.m49_mwh)
                    )
                }
            }

            else -> {
                Pair(
                    "0",
                    WECOApplication.instance().getString(R.string.m48_kwh)
                )
            }


        }
    }

    /**
     * 功率单位(平均)：数值转换，基础单位是w
     */
    fun valueFromW(wValue: Double?): Pair<String, String> {
        return when (wValue) {
            null -> {
                Pair("0", WECOApplication.instance().getString(R.string.m126_w))
            }

            in (0.0..10000.0) -> {
                Pair(
                    Util.getDoubleText(wValue / 1000),
                    WECOApplication.instance().getString(R.string.m126_w)
                )
            }


            in (10000.0..1000000.0) -> {
                Pair(
                    Util.getDoubleText(wValue / 1000),
                    WECOApplication.instance().getString(R.string.m52_kw)
                )
            }


            else -> {
                Pair(
                    Util.getDoubleText(wValue / 1000000),
                    WECOApplication.instance().getString(R.string.m53_mw)
                )
            }
        }
    }

    /**
     * 功率单位（峰值）：数值转换，基础单位是w
     * 目前只有组件总功率使用到
     */
    fun valueFromWp(wValue: Double?): Pair<String, String> {
        return when {
            wValue == null -> {
                Pair("0", WECOApplication.instance().getString(R.string.m50_kwp))
            }
            wValue < 100000000 -> {
                Pair(
                    Util.getDoubleText(wValue / 1000),
                    WECOApplication.instance().getString(R.string.m50_kwp)
                )
            }
            else -> {
                Pair(
                    Util.getDoubleText(wValue / 1000000),
                    WECOApplication.instance().getString(R.string.m51_mwp)
                )
            }
        }
    }

    /**
     * 重量单位：数值转换，基础单位是kg
     */
    fun valueFromKG(kgValue: Double?): Pair<String, String> {
        return when {
            kgValue == null -> {
                Pair("0", WECOApplication.instance().getString(R.string.m59_kg))
            }
            kgValue < 100000 -> {
                Pair(
                    Util.getDoubleText(kgValue),
                    WECOApplication.instance().getString(R.string.m59_kg)
                )
            }
            else -> {
                Pair(
                    Util.getDoubleText(kgValue / 1000),
                    WECOApplication.instance().getString(R.string.m60_t)
                )
            }
        }
    }


    /**
     * @param f   源数据
     * @param num 保留的位数
     * @return
     */
    fun roundDouble2String(f: Double, num: Int): String? {
        val bg = BigDecimal(f)
        val f1: Double = bg.setScale(num, BigDecimal.ROUND_HALF_UP).toDouble()
        return f1.toString()
    }


}
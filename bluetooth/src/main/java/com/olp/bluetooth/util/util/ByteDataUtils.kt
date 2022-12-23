package com.ttech.bluetooth.util.util

import java.util.*
import kotlin.experimental.and

object ByteDataUtils {


    /**
     * 将byte[2]转成byte[2]
     *
     * @return
     */
    fun byte2Int(b: ByteArray): Int {
        var value = 0
        if (b.size > 1) {
            value = 0x000000ff and b[0].toInt() shl 8 and 0x0000ff00 or (0x000000ff and b[1].toInt())
            return value
        }
        return value
    }


    /**
     * byte数组转为String
     */
    fun bytesToHexString(bytes: ByteArray): String {
        val result = StringBuilder()
        for (aByte in bytes) {
            var hexString = Integer.toHexString((aByte and 0xFF.toByte()).toInt())
            if (hexString.length == 1) {
                hexString = "0$hexString"
            }
            result.append(hexString.uppercase(Locale.getDefault()))
        }
        return result.toString()
    }


    /**
     * 将int转成byte[2]
     *
     * @param a
     * @return
     */
    fun int2Byte(a: Int): ByteArray {
        val b = ByteArray(2)
        b[0] = (a shr 8).toByte()
        b[1] = a.toByte()
        return b
    }



    fun getMsgByAes(bytes: ByteArray?): ByteArray {
        val bytes1: ByteArray? = bytes?.let { getbyte16(it, 16) }
        return AESCBCUtils.AESEncryption(bytes1)
    }


    /**
     * 将数据补够bitnum的倍数
     *
     * @param bitNum
     * @return
     */
    fun getbyte16(data: ByteArray, bitNum: Int): ByteArray {
        val length = data.size
        val multiple = length / bitNum

        //目标长度
        val tagetLent = (multiple + 1) * bitNum
        val newData = ByteArray(tagetLent)
        if (length % bitNum == 0) {
            return data
        } else {
            System.arraycopy(data, 0, newData, 0, length)
        }
        return newData
    }


    fun byteToString(bytes: ByteArray?): String {
        if (bytes == null) return ""
        val strBuilder = java.lang.StringBuilder()
        for (aByte in bytes) {
            if (aByte.toInt() != 0) {
                strBuilder.append(Char(aByte.toUShort()))
            } else {
                break
            }
        }
        return strBuilder.toString()
    }


    fun bytes2Int(bytes: ByteArray): Int {
        //将每个byte依次搬运到int相应的位置
        var result: Int = (bytes[0] and 0xff.toByte()).toInt()
        result = result shl 8 or bytes[1].toInt() and 0xff
        return result
    }


}
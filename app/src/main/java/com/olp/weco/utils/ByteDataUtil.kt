package com.olp.weco.utils

import com.olp.bluetooth.util.CRC16
import com.olp.weco.app.WECOApplication.Companion.APP_NAME
import com.olp.weco.model.ble.DatalogAPSetParam
import com.olp.weco.model.ble.DatalogBlueMsgBean
import com.olp.weco.model.ble.DatalogResponBean
import com.olp.weco.model.ble.DatalogResponBean.ParamBean
import com.ttech.bluetooth.util.util.ByteDataUtils.byteToString
import com.ttech.bluetooth.util.util.ByteDataUtils.bytes2Int
import com.ttech.bluetooth.util.util.ByteDataUtils.getMsgByAes
import com.ttech.bluetooth.util.util.ByteDataUtils.int2Byte
import com.olp.lib.util.LogUtil


object ByteDataUtil {

    object BlueToothData {
        //获取采集器信息命令
        const val DATALOG_GETDATA_0X19: Byte = 0x19
        const val DATALOG_GETDATA_0X18: Byte = 0x18
        const val DATALOG_GETDATA_0X26: Byte = 0x26

        /**
         * 发送查询命令(0x19)------蓝牙
         *
         * @param deviceId 设备序列号
         * @param valus    查询参数的编号
         * @param fun      功能码
         * @return
         */
        fun numberServerPro18(
            `fun`: Byte,
            deviceId: String,
            valus: List<DatalogAPSetParam?>
        ): ByteArray {
            val msgBean = DatalogBlueMsgBean()

            //总长度=协议标识+实际数据长度+设备地址+功能码+数据区内容+AES加密补零区+CRC16码
            //协议标识固定06
            //实际数据长度=设备地址+功能码+数据区内容（不包含补0的）
            msgBean.device_addr = DatalogBlueMsgBean.DEVICE_CODE
            msgBean.fun_code = `fun`
            //数据区
            //要设置的数据
            val bytes: ByteArray = parseBean2Byte(valus)
            val length = bytes.size

            //采集器序列号
            val serialBytes = deviceId.toByteArray()
            //参数编号个数
            val size = valus.size
            val sizeByte: ByteArray = int2Byte(size)
            //设置数据长度
            val lengthByte: ByteArray = int2Byte(length)

            //设置数据

            //有效数据
            val alllen = serialBytes.size + sizeByte.size + lengthByte.size + bytes.size
            val allDataBytes = ByteArray(alllen)
            System.arraycopy(serialBytes, 0, allDataBytes, 0, serialBytes.size)
            System.arraycopy(sizeByte, 0, allDataBytes, serialBytes.size, sizeByte.size)
            System.arraycopy(
                lengthByte,
                0,
                allDataBytes,
                serialBytes.size + sizeByte.size,
                lengthByte.size
            )
            System.arraycopy(
                bytes,
                0,
                allDataBytes,
                serialBytes.size + sizeByte.size + lengthByte.size,
                bytes.size
            )


            //对数据补0(16的倍数) 并进行AES加密
            val encryptedData: ByteArray = getMsgByAes(allDataBytes)
            msgBean.data = encryptedData

            //实际长度
            val realLen = 1 + 1 + alllen
            val realLenByte: ByteArray = int2Byte(realLen)
            msgBean.realdata_len = realLenByte

            //总长度=协议标识+实际数据长度+设备地址+功能码+数据区内容+AES加密补零区+CRC16码
            val allLen = 2 + 2 + 1 + 1 + encryptedData.size + 2
            val allLenByte: ByteArray = int2Byte(allLen)
            msgBean.all_len = allLenByte

            //没有CRC的数据
            val data: ByteArray = msgBean.bytes

            //获取crc效验
            val crc = CRC16.calcCrc16(data)
            val crcBytes: ByteArray = int2Byte(crc)
            msgBean.crcData = crcBytes
            return msgBean.bytesCRC
        }


        /**
         * 发送查询命令(0x19)------蓝牙
         *
         * @param deviceId 设备序列号
         * @param valus    查询参数的编号
         * @param fun      功能码
         * @return
         */
        fun numberServerPro19(`fun`: Byte, deviceId: String, valus: IntArray): ByteArray? {
            val msgBean = DatalogBlueMsgBean()

            //总长度=协议标识+实际数据长度+设备地址+功能码+数据区内容+AES加密补零区+CRC16码
            //协议标识固定06
            //实际数据长度=设备地址+功能码+数据区内容（不包含补0的）
            msgBean.device_addr = DatalogBlueMsgBean.DEVICE_CODE
            msgBean.fun_code = `fun`
            //数据区
            //采集器序列号
            val serialBytes = deviceId.toByteArray()
            //参数编号个数
            val paramNum = valus.size
            val funNumByte = int2Byte(paramNum)
            //设置数据
            val dataByte = ByteArray(valus.size * 2)
            for (i in valus.indices) {
                val valus1 = valus[i]
                val bytes1 = int2Byte(valus1)
                System.arraycopy(bytes1, 0, dataByte, bytes1.size * i, bytes1.size)
            }
            //有效数据
            val alllen = serialBytes.size + funNumByte.size + dataByte.size
            val allDataBytes = ByteArray(alllen)
            System.arraycopy(serialBytes, 0, allDataBytes, 0, serialBytes.size)
            System.arraycopy(funNumByte, 0, allDataBytes, serialBytes.size, funNumByte.size)
            System.arraycopy(
                dataByte,
                0,
                allDataBytes,
                serialBytes.size + funNumByte.size,
                dataByte.size
            )

            //对数据补0(16的倍数) 并进行AES加密
            val encryptedData = getMsgByAes(allDataBytes)
            msgBean.data = encryptedData

            //实际内容长度=设备地址+功能码+数据内容长度
            val realLen = 1 + 1 + alllen
            val realLenByte = int2Byte(realLen)
            msgBean.realdata_len = realLenByte

            //总长度=协议标识+实际数据长度+设备地址+功能码+数据区内容+AES加密补零区+CRC16码
            val allLen = 2 + 2 + 1 + 1 + encryptedData.size + 2
            val allLenByte = int2Byte(allLen)
            msgBean.all_len = allLenByte

            //没有CRC的数据
            val data = msgBean.bytes

            //获取crc效验
            val crc = CRC16.calcCrc16(data)
            val crcBytes = int2Byte(crc)
            msgBean.crcData = crcBytes
            return msgBean.bytesCRC
        }







        private fun parseBean2Byte(valus: List<DatalogAPSetParam?>): ByteArray {
            //1.将数据集合的转成数组集合
            val byteList: MutableList<ByteArray> = ArrayList()
            //2.记录所有数据的byte长度
            var len = 0
            //3.将集合中的数据转成byte数组，然后保存
            for (i in valus.indices) {
                val datalogAPSetParam = valus[i]
                val value = datalogAPSetParam?.value
                val paramnum = datalogAPSetParam?.paramnum ?: 0
                val paramBytes = int2Byte(paramnum)
                val valueBytes = value?.toByteArray()
                val length = valueBytes?.size ?: 0
                val lenBytes = int2Byte(length)
                val bytes = ByteArray(paramBytes.size + valueBytes!!.size + lenBytes.size)
                len += paramBytes.size + valueBytes.size + lenBytes.size
                System.arraycopy(paramBytes, 0, bytes, 0, paramBytes.size)
                System.arraycopy(lenBytes, 0, bytes, paramBytes.size, lenBytes.size)
                System.arraycopy(
                    valueBytes,
                    0,
                    bytes,
                    paramBytes.size + lenBytes.size,
                    valueBytes.size
                )
                byteList.add(bytes)
            }
            //4.用于返回封装好的数组
            val allBeanBytes = ByteArray(len)
            //5.开始封装数据byte数组
            var len2 = 0
            for (i in byteList.indices) {
                val bytes = byteList[i]
                System.arraycopy(bytes, 0, allBeanBytes, len2, bytes.size)
                len2 += bytes.size
            }
            return allBeanBytes
        }


        /**
         * 去除外部协议，只留实际内容,0x18命令
         */
        fun removePro(bytes: ByteArray?): ByteArray? {
            if (bytes == null) return null
            val len = bytes.size
            if (len > 8) {
                val bs = ByteArray(len - 8)
                System.arraycopy(bytes, 8, bs, 0, len - 8)
                val noCrc = ByteArray(bs.size - 2)
                System.arraycopy(bs, 0, noCrc, 0, noCrc.size)
                return noCrc
            }
            return bytes
        }



        //解析数据

        /**
         * @param bytes
         * @return
         * @throws Exception
         */
        fun paserData(byte: Byte,bytes: ByteArray?): DatalogResponBean? {
            if (bytes == null || bytes.size < 8) return null
            val bean: DatalogResponBean? = when (byte) {
                DATALOG_GETDATA_0X18 -> {
                    parserfun0x18(bytes)
                }
                DATALOG_GETDATA_0X19 -> {
                    parserfun0x19(bytes)
                }
                else -> {
                    parserfun0x26(bytes)
                }
            }
            bean?.funcode = byte
            return bean
        }


        /**
         * 解析0x19应答
         *
         * @param bytes
         * @return
         */
        fun parserfun0x19(bytes: ByteArray?): DatalogResponBean? {
            if (bytes == null || bytes.size < 13) return null
            val bean = DatalogResponBean()
            //设备序列号10个字节
            val serialNumByte = ByteArray(10)
            System.arraycopy(bytes, 0, serialNumByte, 0, serialNumByte.size)
            val dataLogSerial: String = byteToString(serialNumByte)
            bean.datalogSerial = dataLogSerial
            LogUtil.i(APP_NAME,"设备序列号：$dataLogSerial")
            //参数编号个数2个字节
            val paramNumByte = ByteArray(2)
            System.arraycopy(bytes, serialNumByte.size, paramNumByte, 0, paramNumByte.size)
            val paramNum: Int = bytes2Int(paramNumByte)
            bean.paramNum = paramNum
            LogUtil.i(APP_NAME,"参数编号个数：$paramNum")
            //状态码1个字节
            val statusCodeByte = ByteArray(1)
            System.arraycopy(
                bytes,
                serialNumByte.size + paramNumByte.size,
                statusCodeByte,
                0,
                statusCodeByte.size
            )
            val statusCode = statusCodeByte[0].toInt()
            bean.statusCode = statusCode
            LogUtil.i(APP_NAME,"状态码：$statusCode")
            //参数编号对应的数据
            val datas = ByteArray(bytes.size - 13)
            System.arraycopy(
                bytes,
                serialNumByte.size + paramNumByte.size + statusCodeByte.size,
                datas,
                0,
                datas.size
            )
            val datalist: MutableList<ParamBean> = ArrayList()

            //记录当前长度
            var allLenth = 0
            for (i in 0 until paramNum) {
                //参数编号2个字节
                val numByte = ByteArray(2)
                System.arraycopy(datas, allLenth, numByte, 0, numByte.size)
                val num: Int = bytes2Int(numByte)
                LogUtil.i(APP_NAME,"参数编号：$num")
                //参数长度2个字节
                val lengthByte = ByteArray(2)
                System.arraycopy(datas, allLenth + numByte.size, lengthByte, 0, lengthByte.size)
                val length: Int = bytes2Int(lengthByte)
                LogUtil.i(APP_NAME,"数据长度：$length")
                //数据
                val valueByte = ByteArray(length)
                System.arraycopy(
                    datas,
                    allLenth + numByte.size + lengthByte.size,
                    valueByte,
                    0,
                    length
                )
                var value = ""
                /*    if (num==FOTA_FILE_TYPE){//转成int
                 value=String.valueOf(bytes2Int(valueByte));
            }else {
                 value = SmartHomeUtil.ByteToString(valueByte);
            }*/value = byteToString(valueByte)
                LogUtil.i(APP_NAME,"对应数据：$value")
                val paramBean = ParamBean()
                paramBean.length = length
                paramBean.num = num
                paramBean.value = value
                datalist.add(paramBean)
                allLenth += 4 + length
            }
            bean.paramBeanList = datalist
            return bean
        }


        /**
         * 解析0x18应答
         *
         * @param bytes
         * @return
         */
        fun parserfun0x18(bytes: ByteArray?): DatalogResponBean? {
            if (bytes == null || bytes.size < 13) return null
            val bean = DatalogResponBean()
            //设备序列号10个字节
            val serialNumByte = ByteArray(10)
            System.arraycopy(bytes, 0, serialNumByte, 0, serialNumByte.size)
            val dataLogSerial: String = byteToString(serialNumByte)
            bean.datalogSerial = dataLogSerial
            LogUtil.i(APP_NAME,"设备序列号：$dataLogSerial")
            //参数编号个数2个字节
            val paramNumByte = ByteArray(2)
            System.arraycopy(bytes, serialNumByte.size, paramNumByte, 0, paramNumByte.size)
            val paramNum: Int = bytes2Int(paramNumByte)
            bean.paramNum = paramNum
            LogUtil.i(APP_NAME,"参数编号个数：$paramNum")
            //状态码1个字节
            val statusCodeByte = ByteArray(1)
            System.arraycopy(
                bytes,
                serialNumByte.size + paramNumByte.size,
                statusCodeByte,
                0,
                statusCodeByte.size
            )
            val statusCode = statusCodeByte[0].toInt()
            bean.statusCode = statusCode
            LogUtil.i(APP_NAME,"状态码：$statusCode")
            return bean
        }


        /**
         * 解析0x26应答
         *
         * @param bytes
         * @return
         */
        fun parserfun0x26(bytes: ByteArray?): DatalogResponBean? {
            if (bytes == null || bytes.size < 10) return null
            val bean = DatalogResponBean()
            //设备序列号10个字节
            val serialNumByte = ByteArray(10)
            System.arraycopy(bytes, 0, serialNumByte, 0, serialNumByte.size)
            val dataLogSerial: String = byteToString(serialNumByte)
            bean.datalogSerial = dataLogSerial
            LogUtil.i(APP_NAME,"设备序列号：$dataLogSerial")
            val datas = ByteArray(bytes.size - 10)
            System.arraycopy(bytes, serialNumByte.size, datas, 0, datas.size)
            //参数编号个数2个字节
            val datalist: MutableList<ParamBean> = ArrayList()
            //记录当前长度
            //文件数据分包总数量
            val totalByte = ByteArray(2)
            System.arraycopy(datas, 0, totalByte, 0, totalByte.size)
            val total: Int = bytes2Int(totalByte)
            LogUtil.i(APP_NAME,"文件数据分包总数量：$total")
            //当前数据包编号
            val numByte = ByteArray(2)
            System.arraycopy(datas, totalByte.size, numByte, 0, numByte.size)
            val num: Int = bytes2Int(numByte)
            LogUtil.i(APP_NAME,"当前数据包编号：$num")
            //当前数据包接收状态码
            //状态码1个字节
            val statusCodeByte = ByteArray(1)
            System.arraycopy(
                datas,
                totalByte.size + numByte.size,
                statusCodeByte,
                0,
                statusCodeByte.size
            )
            val statusCode = statusCodeByte[0].toInt()
            LogUtil.i(APP_NAME,"状态码：$statusCode")
            val paramBean = ParamBean()
            paramBean.totalLength = total
            paramBean.dataNum = num
            paramBean.dataCode = statusCode
            datalist.add(paramBean)
            bean.paramBeanList = datalist
            return bean
        }





    }


}
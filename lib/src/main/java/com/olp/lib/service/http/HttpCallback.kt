package com.olp.lib.service.http

import  com.olp.lib.LibApplication
import com.olp.lib.util.GsonManager
import com.olp.lib.util.ToastUtil
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

abstract class HttpCallback<R> : IHttpCallback {

    override fun onSuccess(response: String?) {
        //统一处理登录失效的问题
        try {
            response?.also {
                val result = JSONObject(response).optString("result").toString()
                if (result == "10000") {
                    //做自动登录处理
                    LibApplication.instance().accountService().tokenExpired()
                    val errorMsg = JSONObject(response).optString("msg").toString()
                    ToastUtil.show(errorMsg)
                    return
                }


            }
        } catch (e: JSONException) {
        }

        val result: R? = GsonManager.fromJsonType(response, getType())

        if (result == null) {
            onFailure(
                HttpErrorModel.ERROR_CODE_PARSE,
                LibApplication.instance().getString(com.olp.lib.R.string.parse_error)
            )
        } else {
            success(result)
        }
    }

    abstract fun success(result: R)

    private fun getType(): Type {
        val parameterizedType = javaClass.genericSuperclass as ParameterizedType
        return parameterizedType.actualTypeArguments[0]
    }

    override fun onFailure(errorCode: String, error: String?) {
        onFailure(HttpErrorModel(errorCode, error))
    }

    open fun onFailure(errorModel: HttpErrorModel) {}
}
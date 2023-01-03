package com.olp.weco.ui.account.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.olp.weco.base.BaseViewModel
import com.olp.weco.service.http.ApiPath
import com.olp.lib.service.account.User
import com.olp.lib.service.http.HttpCallback
import com.olp.lib.service.http.HttpErrorModel
import com.olp.lib.service.http.HttpResult
import com.olp.lib.util.MD5Util
import kotlinx.coroutines.launch

class RegisterViewModel : BaseViewModel() {

    val registerLiveData = MutableLiveData<Pair<User?, String?>>()

    /**
     * 是否同意隐私政策
     */
    var isAgree = false


    var selectArea: String = ""


    /**
     * 注册
     */
    fun register(
        country: String, timeZone: String,
        email: String, password: String,
        verificationCode: String, installerCode: String
    ) {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("country", country)
                put("timeZone", timeZone)
                put("email", email)
                put("password", MD5Util.md5(password) ?: "")
                put("verificationCode", verificationCode)
                put("installerCode", installerCode)

            }
            apiService().postForm(ApiPath.Mine.REGISTER, params, object :
                HttpCallback<HttpResult<User>>() {
                override fun success(result: HttpResult<User>) {
                    if (result.isBusinessSuccess()) {

                        val user = result.obj
                        user?.password = password

                        registerLiveData.value = Pair(user, result.msg)
                    } else {
                        registerLiveData.value = Pair(null, result.msg)
                    }
                }

                override fun onFailure(errorModel: HttpErrorModel) {
                    registerLiveData.value = Pair(null, errorModel.errorMsg)
                }

            })
        }
    }


}
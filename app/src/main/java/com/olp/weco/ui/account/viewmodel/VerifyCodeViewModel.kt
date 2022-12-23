package com.olp.weco.ui.account.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.olp.weco.base.BaseViewModel
import com.olp.weco.service.http.ApiPath
import com.olp.lib.service.http.HttpCallback
import com.olp.lib.service.http.HttpErrorModel
import com.olp.lib.service.http.HttpResult
import kotlinx.coroutines.launch

/**
 * 验证码相关
 * 1.获取验证码
 * 2.验证验证码
 */
class VerifyCodeViewModel : BaseViewModel() {

    val getVerifyCodeLiveData = MutableLiveData<Pair<Boolean,String?>>()

    val TIME_COUT=180



    /**
     * 获取验证码
     * @param phoneOrEmailStr 手机号或者邮箱
     */
    fun fetchVerifyCode(email: String,type:String) {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("email", email)
                put("type", type)
            }
            apiService().postForm(ApiPath.Mine.SENDEMAILCODE, params, object :
                HttpCallback<HttpResult<String>>() {
                override fun success(result: HttpResult<String>) {
                    getVerifyCodeLiveData.value =Pair(result.isBusinessSuccess(),result.msg ?: "")
                }
                override fun onFailure(errorModel: HttpErrorModel) {
                    getVerifyCodeLiveData.value = Pair(false,errorModel.errorMsg ?: "")
                }

            })
        }
    }



}
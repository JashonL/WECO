package com.olp.weco.ui.account.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.olp.lib.service.http.HttpCallback
import com.olp.lib.service.http.HttpErrorModel
import com.olp.lib.service.http.HttpResult
import com.olp.weco.base.BaseViewModel
import com.olp.weco.service.http.ApiPath
import kotlinx.coroutines.launch

class AccountViewModel : BaseViewModel() {

    val logoutLiveData = MutableLiveData<String?>()

    /**
     * 登出
     */
    fun logout(email: String?) {

        val params = hashMapOf<String, String>().apply {
            put("email", email ?: "")
        }

        viewModelScope.launch {
            apiService().postForm(
                ApiPath.Mine.LOGOUT,
                params,
                object : HttpCallback<HttpResult<String>>() {
                    override fun success(result: HttpResult<String>) {
                        if (result.isBusinessSuccess()) {
                            logoutLiveData.value = null
                        } else {
                            logoutLiveData.value = result.msg ?: ""
                        }
                    }

                    override fun onFailure(errorModel: HttpErrorModel) {
                        logoutLiveData.value = errorModel.errorMsg ?: ""
                    }

                })
        }
    }
}
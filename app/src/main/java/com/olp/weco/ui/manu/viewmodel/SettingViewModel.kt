package com.olp.weco.ui.manu.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.olp.weco.base.BaseViewModel
import com.olp.weco.service.http.ApiPath
import com.olp.lib.service.http.HttpCallback
import com.olp.lib.service.http.HttpErrorModel
import com.olp.lib.service.http.HttpResult
import com.olp.lib.util.MD5Util
import kotlinx.coroutines.launch
import java.io.File

/**
 * 选择国家/地区
 */
class SettingViewModel : BaseViewModel() {

    val userAvatarLiveData = MutableLiveData<Pair<String?, String?>>()
    val logoutLiveData = MutableLiveData<String?>()
    val modifyPasswordLiveData = MutableLiveData<String?>()
    val changePhoneOrEmailLiveData = MutableLiveData<String?>()
    val modifyInstallerNoLiveData = MutableLiveData<String?>()
    val cancelAccountLiveData = MutableLiveData<String?>()

    val uploadUserAvatarLiveData = MutableLiveData<Pair<String?, String?>>()





    /**
     * 设置-修改密码
     */
    fun modifyPassword(oldPassword: String, newPassword: String) {
        val params = hashMapOf<String, String>().apply {
            put("OldPWD", MD5Util.md5(oldPassword) ?: "")
            put("NewPWD", MD5Util.md5(newPassword) ?: "")
        }

        viewModelScope.launch {
            apiService().postForm(
                ApiPath.Mine.MODIFY_PASSWORD, params,
                object : HttpCallback<HttpResult<String>>() {
                    override fun success(result: HttpResult<String>) {
                        if (result.isBusinessSuccess()) {
                            modifyPasswordLiveData.value = null
                        } else {
                            modifyPasswordLiveData.value = result.msg ?: ""
                        }
                    }

                    override fun onFailure(errorModel: HttpErrorModel) {
                        modifyPasswordLiveData.value = errorModel.errorMsg ?: ""
                    }

                })
        }
    }


    /**
     * 登出
     */
    fun logout() {
        viewModelScope.launch {
            apiService().post(
                ApiPath.Mine.LOGOUT,
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






    /**
     * 设置-上传用户头像
     */
    fun uploadUserAvatar(filePath: String) {
        viewModelScope.launch {
            apiService().postFile(
                ApiPath.Mine.UPLOADAVATAR, hashMapOf(), File(filePath),
                object : HttpCallback<HttpResult<String>>() {
                    override fun success(result: HttpResult<String>) {
                        if (result.isBusinessSuccess()) {
                            uploadUserAvatarLiveData.value = Pair(result.obj, result.msg ?: "")
                        } else {
                            uploadUserAvatarLiveData.value = Pair(null, result.msg ?: "")
                        }
                    }

                    override fun onFailure(errorModel: HttpErrorModel) {
                        uploadUserAvatarLiveData.value = Pair(null, errorModel.errorMsg ?: "")
                    }

                })
        }
    }


}
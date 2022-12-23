package com.olp.weco.base

import androidx.lifecycle.ViewModel
import com.olp.lib.LibApplication
import com.olp.lib.service.ServiceManager
import com.olp.lib.service.account.IAccountService
import com.olp.lib.service.device.IDeviceService
import com.olp.lib.service.http.IHttpService
import com.olp.lib.service.location.ILocationService
import com.olp.lib.service.storage.IStorageService

open class BaseViewModel : ViewModel(), ServiceManager.ServiceInterface {

    override fun apiService(): IHttpService {
        return LibApplication.instance().apiService()
    }

    override fun storageService(): IStorageService {
        return LibApplication.instance().storageService()
    }

    override fun deviceService(): IDeviceService {
        return LibApplication.instance().deviceService()
    }

    override fun accountService(): IAccountService {
        return LibApplication.instance().accountService()
    }

    override fun locationService(): ILocationService {
        return LibApplication.instance().locationService()
    }

}
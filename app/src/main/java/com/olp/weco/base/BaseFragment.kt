package com.olp.weco.base

import android.view.View
import androidx.fragment.app.Fragment
import com.olp.lib.LibApplication
import com.olp.lib.service.ServiceManager
import com.olp.lib.service.account.IAccountService
import com.olp.lib.service.device.IDeviceService
import com.olp.lib.service.http.IHttpService
import com.olp.lib.service.location.ILocationService
import com.olp.lib.service.storage.IStorageService
import com.olp.weco.app.WECOApplication

abstract class BaseFragment : Fragment(), ServiceManager.ServiceInterface, IDisplay {


    override fun apiService(): IHttpService {
        return WECOApplication.instance().apiService()
    }

    override fun storageService(): IStorageService {
        return WECOApplication.instance().storageService()
    }

    override fun deviceService(): IDeviceService {
        return WECOApplication.instance().deviceService()
    }

    override fun accountService(): IAccountService {
        return WECOApplication.instance().accountService()
    }

    override fun locationService(): ILocationService {
        return LibApplication.instance().locationService()
    }

    override fun showDialog() {
        (activity as? BaseActivity)?.showDialog()
    }

    override fun dismissDialog() {
        (activity as? BaseActivity)?.dismissDialog()
    }

    override fun showPageErrorView(onRetry: ((view: View) -> Unit)) {
        (activity as? BaseActivity)?.showPageErrorView(onRetry)
    }

    override fun hidePageErrorView() {
        (activity as? BaseActivity)?.hidePageErrorView()
    }

    override fun showPageLoadingView() {
        (activity as? BaseActivity)?.showPageLoadingView()
    }

    override fun hidePageLoadingView() {
        (activity as? BaseActivity)?.hidePageLoadingView()
    }

    override fun showResultDialog(
        result: String?,
        onCancelClick: (() -> Unit)?,
        onComfirClick: (() -> Unit)?
    ) {
        (activity as? BaseActivity)?.showResultDialog(result, onCancelClick, onComfirClick)
    }

}
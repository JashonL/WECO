package com.olp.weco.app

import android.os.Process
import com.olp.weco.ui.launch.fragment.UserAgreementDialog
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.olp.weco.R
import com.olp.weco.service.account.DefaultAccountService
import com.olp.weco.service.http.OkhttpService
import com.olp.lib.LibApplication
import com.olp.lib.service.ServiceManager
import com.olp.lib.service.ServiceType
import com.olp.lib.service.account.IAccountService
import com.olp.lib.service.device.DefaultDeviceService
import com.olp.lib.service.device.IDeviceService
import com.olp.lib.service.http.IHttpService
import com.olp.lib.service.location.ILocationService
import com.olp.lib.service.storage.DefaultStorageService
import com.olp.lib.service.storage.IStorageService
import com.olp.lib.util.Util

class WECOApplication : LibApplication() , ServiceManager.ServiceInterface{



    companion object {
        private lateinit var instance: WECOApplication
        fun instance()= instance
        const val APP_OS = 0
        const val APP_NAME="TENTEK"

        init {
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                layout.setPrimaryColorsId(R.color.color_app_main, R.color.white) //全局设置主题颜色
                MaterialHeader(context)
            }
        }
    }


    override fun onCreate() {
        super.onCreate()
        instance =this
        registerService()
        init()

    }




    private fun init() {
        //过滤掉其它进程
        if (!packageName.equals(Util.getProcessNameByPID(this, Process.myPid()))) {
            return
        }
        registerService()
        Foreground.init(this)


    }



    private fun registerService(){
        ServiceManager.instance().registerService(ServiceType.HTTP, OkhttpService())

        ServiceManager.instance()
            .registerService(ServiceType.STORAGE, DefaultStorageService(this))
        ServiceManager.instance().registerService(ServiceType.DEVICE, DefaultDeviceService(this))
        ServiceManager.instance().registerService(ServiceType.ACCOUNT, DefaultAccountService())

    }




    override fun apiService(): IHttpService {
        return ServiceManager.instance().getService(ServiceType.HTTP) as IHttpService
    }

    override fun storageService(): IStorageService {
        return ServiceManager.instance().getService(ServiceType.STORAGE) as DefaultStorageService
    }

    override fun deviceService(): IDeviceService {
        return ServiceManager.instance().getService(ServiceType.DEVICE) as IDeviceService
    }

    override fun accountService(): IAccountService {
        return ServiceManager.instance().getService(ServiceType.ACCOUNT) as IAccountService
    }

    override fun locationService(): ILocationService {
        return ServiceManager.instance().getService(ServiceType.LOCATION) as ILocationService
    }



    /**
     * 是否同意隐私政策
     */
    fun isAgree(): Boolean {
        return storageService().getBoolean(UserAgreementDialog.KEY_IS_AGREE_AGREEMENT, false)
    }



}
package com.olp.weco.base

import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.olp.lib.service.ServiceManager
import com.olp.lib.service.account.IAccountService
import com.olp.lib.service.device.IDeviceService
import com.olp.lib.service.http.IHttpService
import com.olp.lib.service.location.ILocationService
import com.olp.lib.service.storage.IStorageService
import com.olp.weco.app.WECOApplication

open class BaseViewHolder(
    itemView: View,
    private val onItemClickListener: OnItemClickListener? = null,
) : RecyclerView.ViewHolder(itemView),
    ServiceManager.ServiceInterface, View.OnClickListener, View.OnLongClickListener {

    fun showDialog() {
        (itemView.context as? BaseActivity)?.showDialog()
    }

    fun dismissDialog() {
        (itemView.context as? BaseActivity)?.dismissDialog()
    }

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
        return WECOApplication.instance().locationService()
    }

    override fun onClick(v: View?) {
        onItemClickListener?.onItemClick(v, bindingAdapterPosition)
    }

    fun getColor(@ColorRes colorId: Int): Int {
        return WECOApplication.instance().resources.getColor(colorId)
    }

    fun getString(@StringRes stringId: Int): String {
        return WECOApplication.instance().getString(stringId)
    }

    override fun onLongClick(v: View?): Boolean {
        onItemClickListener?.onItemLongClick(v, bindingAdapterPosition)
        return true
    }


}

interface OnItemClickListener {

    fun onItemClick(v: View?, position: Int) {}

    fun onItemLongClick(v: View?, position: Int) {}
}




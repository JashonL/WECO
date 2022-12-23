package com.olp.weco.service.account

import android.content.Context
import com.olp.lib.service.account.BaseAccountService
import com.olp.weco.app.Foreground
import com.olp.weco.ui.account.login.activity.LoginActivity

class DefaultAccountService : BaseAccountService() {

    override fun login(context: Context) {
        LoginActivity.startClearTask(context)
    }

    override fun tokenExpired() {
        logout()
        Foreground.instance().getTopActivity()?.also {
            login(it)
        }
    }
}
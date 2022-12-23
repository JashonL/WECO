package com.olp.weco.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.viewModels
import com.olp.weco.app.WECOApplication
import com.olp.weco.ui.account.register.RegisterActivity
import com.olp.weco.base.BaseActivity
import com.olp.weco.databinding.ActivityLoginRegisterBinding
import com.olp.weco.ui.account.login.activity.LoginActivity
import com.olp.weco.ui.account.login.viewmodel.LoginViewModel
import com.olp.lib.service.account.User
import com.olp.lib.util.MD5Util
import com.olp.lib.util.ToastUtil
import com.olp.lib.util.Util

class LoginAndRegisterActivity : BaseActivity(),OnClickListener{

    private val viewModel: LoginViewModel by viewModels()

    companion object{
        fun start(context: Context) {
            val intent = Intent(context, LoginAndRegisterActivity::class.java)
            context.startActivity(intent)
        }
    }


    lateinit var binding:ActivityLoginRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        setLiseners()
        initViews()
    }



    private fun initData() {
        viewModel.loginLiveData.observe(this) {
            dismissDialog()
            if (it.second == null) {
                val user = it.first
                loginSuccess(user)
            } else {
                ToastUtil.show(it.second)
            }
        }

    }


    private fun loginSuccess(user: User?) {
        MainActivity.start(this)
        finish()
    }

    private fun initViews() {
        val user = accountService().user()
        val logout = accountService().isLogout()
        user?.let {
            val email = it.email
            val password = it.password
            if (!logout) {
                login(password, email)
            }
        }


    }



    private fun login(password: String, userName: String) {
        if (!TextUtils.isEmpty(password)) {
            showDialog()
            val pwd_md5 = MD5Util.md5(password)
            var version = Util.getVersion(this)
            val phoneModel = Util.getPhoneModel()
            if (version == null) version = "";
            if (pwd_md5 != null) {
                viewModel.login(userName, pwd_md5, WECOApplication.APP_OS, phoneModel, version)
            }
        }

    }


    private fun setLiseners() {
        binding.btLogin.setOnClickListener(this)
        binding.btRegister.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when{
            p0===binding.btLogin-> LoginActivity.start(this)
            p0===binding.btRegister-> RegisterActivity.start(this)
        }
    }


}
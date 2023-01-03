package com.olp.weco.ui.account.forgot

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.olp.weco.R
import com.olp.weco.base.BaseActivity
import com.olp.weco.databinding.ActivityForgotBinding
import com.olp.weco.ui.account.forgot.ViewModel.FotgotPasswordViewModel
import com.olp.weco.ui.account.viewmodel.VerifyCodeViewModel
import com.olp.lib.util.ToastUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ForgotPasswordActivity : BaseActivity(), OnClickListener {

    companion object {
        fun start(context: Context?) {
            context?.startActivity(Intent(context, ForgotPasswordActivity::class.java))
        }
    }

    private lateinit var binding: ActivityForgotBinding


    private val verifyCodeViewModel: VerifyCodeViewModel by viewModels()
    private val forgotPasswordViewModel: FotgotPasswordViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initListeners()
    }




    private fun initListeners() {
        binding.etCode.setOnRightClickListener {
            if (TextUtils.isEmpty(getPhoneOrEmailText())) {
                ToastUtil.show(getString(R.string.m89_no_email))
            } else {
                requestSendVerifyCode()
            }

        }


        binding.etNewpassword.setOnRightClickListener {
            binding.etNewpassword.setEye()
        }

        binding.etComfirPassword.setOnRightClickListener {
            binding.etComfirPassword.setEye()
        }


        binding.btReset.setOnClickListener(this)


    }

    private fun getPhoneOrEmailText() = binding.etEmail.getValue()

    private fun requestSendVerifyCode() {
        val phoneOrEmail = getPhoneOrEmailText()
        phoneOrEmail?.let {
            verifyCodeViewModel.fetchVerifyCode(phoneOrEmail,"2")
        }
    }


    private fun initData() {
        verifyCodeViewModel.getVerifyCodeLiveData.observe(this) {
            dismissDialog()
            ToastUtil.show(it.second)
            if (it.second == null) {
                updateCountDown()
            }
        }


        forgotPasswordViewModel.resetPasswordListdata.observe(this){
            ToastUtil.show(it.second)
        }

    }


    private fun updateCountDown() {
        lifecycleScope.launch {
            binding.etCode.setRightEnable(false)
            for (i in verifyCodeViewModel.TIME_COUT downTo 0) {
                if (i == 0) {
                    binding.etCode.setRightEnable(true)
                    binding.etCode.setRightText(getString(R.string.m158_send_verify_code))
                } else {
                    binding.etCode.setRightText(
                        getString(
                            R.string.m160_second_after_send,
                            i
                        )
                    )
                    delay(1000)
                }
            }
        }

    }

    override fun onClick(p0: View?) {
        when {
            p0 === binding.btReset -> resetPassword()
        }

    }


    private fun resetPassword() {
        val email = binding.etEmail.getValue()
        val code = binding.etCode.getValue()
        val newPassword = binding.etNewpassword.getValue()
        val comfirPass = binding.etComfirPassword.getValue()


        when{
            TextUtils.isEmpty(email)-> ToastUtil.show(getString(R.string.m74_please_input_username))
            TextUtils.isEmpty(code)-> ToastUtil.show(getString(R.string.m159_please_verify_code))
            TextUtils.isEmpty(newPassword)-> ToastUtil.show(getString(R.string.m76_password_cant_empty))
            TextUtils.isEmpty(comfirPass)-> ToastUtil.show(getString(R.string.m76_password_cant_empty))
            else ->  forgotPasswordViewModel.retrievePassword(email!!,code!!,newPassword!!,comfirPass!!)

        }




    }


}
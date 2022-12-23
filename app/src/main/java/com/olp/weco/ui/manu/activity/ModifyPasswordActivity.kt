package com.olp.weco.ui.manu.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.activity.viewModels
import com.olp.weco.R
import com.olp.weco.base.BaseActivity
import com.olp.weco.databinding.ActivityModifyPasswordBinding
import com.olp.weco.ui.manu.viewmodel.SettingViewModel
import com.olp.lib.util.ToastUtil
import com.olp.lib.util.setViewHeight

/**
 * 设置-修改密码页面
 */
class ModifyPasswordActivity : BaseActivity(), View.OnClickListener {

    companion object {

        fun start(context: Context?) {
            val intent = Intent(context, ModifyPasswordActivity::class.java)
            context?.startActivity(intent)
        }

    }

    private lateinit var binding: ActivityModifyPasswordBinding

    //是否显示密码
    private var isShowOldPassword = false
    private var isShowPassword = false
    private var isShowConfirmPassword = false
    private var focusOnOldPassword = false
    private var focusOnConfirmPassword = false
    private var focusOnPassword = false

    private val viewModel: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModifyPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
        setListener()
    }

    private fun initData() {
        viewModel.modifyPasswordLiveData.observe(this) {
            dismissDialog()
            if (it == null) {
                finish()
            } else {
                ToastUtil.show(it)
            }
        }
    }

    private fun setListener() {
        binding.ivOldPasswordEye.setOnClickListener(this)
        binding.ivPasswordEye.setOnClickListener(this)
        binding.ivConfirmPasswordEye.setOnClickListener(this)
        binding.etOldPassword.setOnFocusChangeListener { v, hasFocus ->
            focusOnOldPassword = hasFocus
            updateFocusView()
        }
        binding.etPassword.setOnFocusChangeListener { v, hasFocus ->
            focusOnPassword = hasFocus
            updateFocusView()
        }
        binding.etConfirmPassword.setOnFocusChangeListener { v, hasFocus ->
            focusOnConfirmPassword = hasFocus
            updateFocusView()
        }
        binding.title.setOnRightButtonClickListener {
            val oldPassword = binding.etOldPassword.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()
            if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(password) || TextUtils.isEmpty(
                    confirmPassword
                )
            ) {
                ToastUtil.show(getString(R.string.m28_password_cant_empty))
            } else if (oldPassword.length < 6 || password.length < 6 || confirmPassword.length < 6) {
                ToastUtil.show(getString(R.string.m30_password_cannot_be_less_than_6_digits))
            } else if (password != confirmPassword) {
                ToastUtil.show(getString(R.string.m31_passwords_are_inconsistent))
            } else {
                showDialog()
                viewModel.modifyPassword(oldPassword, password)
            }
        }
    }

    private fun initView() {
        updateFocusView()
        updatePasswordView()
    }

    private fun updateFocusView() {
        if (focusOnOldPassword) {
            binding.etOldPassword.setSelection(binding.etOldPassword.length())
            binding.viewOldPasswordLine.setBackgroundResource(R.color.colorAccent)
            binding.viewOldPasswordLine.setViewHeight(2f)
        } else {
            binding.viewOldPasswordLine.setBackgroundResource(R.color.color_line)
            binding.viewOldPasswordLine.setViewHeight(1f)
        }

        if (focusOnPassword) {
            binding.etPassword.setSelection(binding.etPassword.length())
            binding.viewPasswordLine.setBackgroundResource(R.color.colorAccent)
            binding.viewPasswordLine.setViewHeight(2f)
        } else {
            binding.viewPasswordLine.setBackgroundResource(R.color.color_line)
            binding.viewPasswordLine.setViewHeight(1f)
        }

        if (focusOnConfirmPassword) {
            binding.etConfirmPassword.setSelection(binding.etConfirmPassword.length())
            binding.viewConfirmPasswordLine.setBackgroundResource(R.color.colorAccent)
            binding.viewConfirmPasswordLine.setViewHeight(2f)
        } else {
            binding.viewConfirmPasswordLine.setBackgroundResource(R.color.color_line)
            binding.viewConfirmPasswordLine.setViewHeight(1f)
        }
    }

    private fun updatePasswordView() {
        if (isShowOldPassword) {
            binding.ivOldPasswordEye.setImageResource(R.drawable.ic_eye_open)
            binding.etOldPassword.transformationMethod =
                HideReturnsTransformationMethod.getInstance()
        } else {
            binding.ivOldPasswordEye.setImageResource(R.drawable.ic_eye_close)
            binding.etOldPassword.transformationMethod = PasswordTransformationMethod.getInstance()
        }
        binding.etOldPassword.setSelection(binding.etOldPassword.length())

        if (isShowPassword) {
            binding.ivPasswordEye.setImageResource(R.drawable.ic_eye_open)
            binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            binding.ivPasswordEye.setImageResource(R.drawable.ic_eye_close)
            binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
        }
        binding.etPassword.setSelection(binding.etPassword.length())

        if (isShowConfirmPassword) {
            binding.ivConfirmPasswordEye.setImageResource(R.drawable.ic_eye_open)
            binding.etConfirmPassword.transformationMethod =
                HideReturnsTransformationMethod.getInstance()
        } else {
            binding.ivConfirmPasswordEye.setImageResource(R.drawable.ic_eye_close)
            binding.etConfirmPassword.transformationMethod =
                PasswordTransformationMethod.getInstance()
        }
        binding.etConfirmPassword.setSelection(binding.etConfirmPassword.length())
    }

    override fun onClick(v: View?) {
        when {
            v === binding.ivOldPasswordEye -> {
                isShowOldPassword = !isShowOldPassword
                updatePasswordView()
            }
            v === binding.ivPasswordEye -> {
                isShowPassword = !isShowPassword
                updatePasswordView()
            }
            v === binding.ivConfirmPasswordEye -> {
                isShowConfirmPassword = !isShowConfirmPassword
                updatePasswordView()
            }
        }
    }

}
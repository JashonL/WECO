package com.olp.weco.ui.account.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.olp.weco.R
import com.olp.weco.app.WECOApplication
import com.olp.weco.base.BaseActivity
import com.olp.weco.databinding.ActivityRegisterBinding
import com.olp.weco.ui.account.login.viewmodel.LoginViewModel
import com.olp.weco.ui.account.viewmodel.VerifyCodeViewModel
import com.olp.weco.ui.common.activity.CountryActivity
import com.olp.weco.ui.common.activity.WebActivity
import com.olp.weco.utils.AppUtil
import com.olp.weco.utils.TimeZoneUtil
import com.olp.lib.service.account.User
import com.olp.lib.util.ActivityBridge
import com.olp.lib.util.MD5Util
import com.olp.lib.util.ToastUtil
import com.olp.lib.util.Util
import com.olp.weco.ui.home.HomeActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterActivity : BaseActivity(), View.OnClickListener {

    companion object {
        fun start(context: Context?) {
            context?.startActivity(Intent(context, RegisterActivity::class.java))
        }
    }


    private lateinit var binding: ActivityRegisterBinding

    private val viewModel: RegisterViewModel by viewModels()
    private val verifyCodeViewModel: VerifyCodeViewModel by viewModels()

    private val loginViewModel: LoginViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        setListeners()
        initData()

    }


    private fun updateCountDown() {
        lifecycleScope.launch {
            binding.etVertificationCode.setRightEnable(false)
            for (i in verifyCodeViewModel.TIME_COUT downTo 0) {
                if (i == 0) {
                    binding.etVertificationCode.setRightEnable(true)
                    binding.etVertificationCode.setRightText(getString(R.string.m158_send_verify_code))
                } else {
                    binding.etVertificationCode.setRightText(
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


    private fun initData() {
        viewModel.registerLiveData.observe(this) {
            dismissDialog()
            val first = it.first
            ToastUtil.show(it.second)
            if (first != null) {
                //注册成功，关闭页面返回登录页面
//                finish()
                //去登录
                login(first.password,first.email)
            } else {
                showResultDialog(first, null, null)
            }
        }


        verifyCodeViewModel.getVerifyCodeLiveData.observe(this) {
            dismissDialog()
            ToastUtil.show(it.second)
            updateCountDown()
        }




        loginViewModel.loginLiveData.observe(this) {
            dismissDialog()
            if (it.second == null) {
                val user = it.first
                loginSuccess(user)
            } else {
                ToastUtil.show(it.second)
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
                loginViewModel.login(userName, pwd_md5, WECOApplication.APP_OS, phoneModel, version)
            }
        }

    }


    private fun loginSuccess(user: User?) {
        accountService().saveUserInfo(user)
        HomeActivity.start(this)
        finish()
    }


    private fun initView() {
        binding.tvUserAgreement.run {
            highlightColor = resources.getColor(android.R.color.transparent)
            movementMethod = LinkMovementMethod.getInstance()
            text = getTvSpan()
        }

    }


    private fun getTvSpan(): SpannableString {
        val userAgreement = getString(R.string.m23_user_agreement)
        val privacyPolicy = getString(R.string.m24_privacy_policy)
        val content =
            getString(R.string.m88_agree_company_user_agreement, userAgreement, privacyPolicy)
        return SpannableString(content).apply {
            addColorSpan(this, userAgreement)
            addClickSpan(this, userAgreement) {
                WebActivity.start(this@RegisterActivity, AppUtil.getUserAgreement())
            }
            addColorSpan(this, privacyPolicy)
            addClickSpan(this, privacyPolicy) {
                WebActivity.start(this@RegisterActivity, AppUtil.getPrivacy())
            }
        }
    }


    private fun addColorSpan(spannable: SpannableString, colorSpanContent: String) {
        val span = ForegroundColorSpan(resources.getColor(R.color.red))
        val startPosition = spannable.toString().indexOf(colorSpanContent)
        val endPosition = startPosition + colorSpanContent.length
        spannable.setSpan(span, startPosition, endPosition, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }


    private fun addClickSpan(
        spannable: SpannableString,
        clickSpanContent: String,
        click: (View) -> Unit
    ) {
        val span = object : ClickableSpan() {
            override fun onClick(view: View) {
                click(view)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                //去掉下划线
                ds.isUnderlineText = false
            }
        }
        val startPosition = spannable.toString().indexOf(clickSpanContent)
        val endPosition = startPosition + clickSpanContent.length
        spannable.setSpan(span, startPosition, endPosition, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }


    private fun setListeners() {
        binding.ivSelect.setOnClickListener(this)
        binding.etZone.setOnRightClickListener {
            setTimeZone()
        }
        binding.etCountry.setOnClickListener(this)
        binding.btLogin.setOnClickListener(this)
        binding.etVertificationCode.setOnRightClickListener {
            if (TextUtils.isEmpty(getPhoneOrEmailText())) {
                ToastUtil.show(getString(R.string.m89_no_email))
            } else {
                requestSendVerifyCode()
            }

        }

    }


    private fun requestSendVerifyCode() {
        val phoneOrEmail = getPhoneOrEmailText()
        phoneOrEmail?.let {
            verifyCodeViewModel.fetchVerifyCode(phoneOrEmail, "1")
        }
    }


    private fun getPhoneOrEmailText() = binding.etEmailNumber.getValue()

    override fun onClick(p0: View?) {
        when {
            p0 === binding.ivSelect -> updateSelectView(!viewModel.isAgree)
            p0 === binding.etCountry -> selectArea()
            p0 === binding.btLogin -> checkInputInfo()
        }
    }


    private fun selectArea() {
        ActivityBridge.startActivity(
            this,
            CountryActivity.getIntent(this),
            object : ActivityBridge.OnActivityForResult {
                override fun onActivityForResult(
                    context: Context?,
                    resultCode: Int,
                    data: Intent?
                ) {
                    if (resultCode == RESULT_OK && data?.hasExtra(CountryActivity.KEY_AREA) == true) {
                        viewModel.selectArea = data.getStringExtra(CountryActivity.KEY_AREA) ?: ""
                        binding.etCountry.setValue(viewModel.selectArea)
                        val timeZone = TimeZoneUtil.getTimeZoneByCountry(viewModel.selectArea)
                        binding.etZone.setValue(timeZone)
                    }
                }
            })
    }


    private fun setTimeZone() {
        val timeZone = "GMT ${Util.getTimeZone()}"
        binding.etZone.setValue(timeZone)

    }


    private fun updateSelectView(isAgree: Boolean) {
        viewModel.isAgree = isAgree
    }


    /**
     * 检查输入信息是否合规
     */
    private fun checkInputInfo() {

        val country = binding.etCountry.getValue()
        val timeZone = binding.etZone.getValue()
        val username = binding.etEmailNumber.getValue()
        val code = binding.etVertificationCode.getValue()
        val password = binding.etNewPassword.getValue()
        val confirmPassword = binding.etNewPassword.getValue()
        val installerCoder = binding.etInstallerCode.getValue()

        if (!viewModel.isAgree) {
            ToastUtil.show(getString(R.string.m81_please_check_agree_agreement))
        } else if (TextUtils.isEmpty(username)) {
            ToastUtil.show(getString(R.string.m74_please_input_username))
        } else if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            ToastUtil.show(getString(R.string.m76_password_cant_empty))
        } else if (password?.length!! < 6 || confirmPassword?.length!! < 6) {
            ToastUtil.show(getString(R.string.m84_password_cannot_be_less_than_6_digits))
        } else if (password != confirmPassword) {
            ToastUtil.show(getString(R.string.m83_passwords_are_inconsistent))
        } else if (TextUtils.isEmpty(country)) {
            ToastUtil.show(getString(R.string.m87_no_country))
        } else if (TextUtils.isEmpty(timeZone)) {
            ToastUtil.show(getString(R.string.m86_no_timezone))
        } else if (TextUtils.isEmpty(code)) {
            ToastUtil.show(getString(R.string.m159_please_verify_code))
        } else if (TextUtils.isEmpty(installerCoder)) {
            ToastUtil.show(getString(R.string.m15_installer_code_not_empty))
        } else {
            showDialog()

            viewModel.register(
                country!!,
                timeZone!!,
                username!!,
                password,
                code!!,
                installerCoder!!
            )
        }
    }


}
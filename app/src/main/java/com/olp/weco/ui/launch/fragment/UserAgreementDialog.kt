package com.olp.weco.ui.launch.fragment

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.olp.weco.ui.common.activity.WebActivity
import com.olp.weco.ui.common.viewmodel.StaticResourceViewModel
import com.olp.weco.ui.launch.monitor.UserAgreementMonitor
import com.olp.weco.R
import com.olp.weco.app.WECOApplication
import com.olp.weco.base.BaseDialogFragment
import com.olp.weco.databinding.DialogUserAgreementBinding
import com.olp.lib.util.ToastUtil
import com.olp.lib.util.gone
import com.olp.lib.util.visible

/**
 * 用户隐私协议弹框
 */
class UserAgreementDialog : BaseDialogFragment(), View.OnClickListener {

    companion object {

        const val KEY_IS_AGREE_AGREEMENT = "key_is_agree_agreement"

        fun showDialog(fm: FragmentManager) {
            val dialog = UserAgreementDialog()
            dialog.show(fm, UserAgreementDialog::class.java.name)
        }
    }

    private lateinit var binding: DialogUserAgreementBinding
    private var isShowTipView = false

    private val staticResourceViewModel: StaticResourceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogUserAgreementBinding.inflate(inflater, container, false)
        //点击返回键dialog不消失
        requireDialog().setCancelable(false)
        //点击外面dialog不消失
        requireDialog().setCanceledOnTouchOutside(false)
        initData()
        initView()
        updateView()
        return binding.root
    }

    private fun initData() {
        staticResourceViewModel.staticResourceLiveData.observe(this) {
            dismissDialog()
            if (it.second == null) {
                WebActivity.start(requireContext(), it.first)
            } else {
                ToastUtil.show(it.second)
            }
        }
    }

    private fun updateView() {
        if (isShowTipView) {
            binding.title.text = getString(R.string.m22_privacy_dialog_tip)
            binding.llContent.gone()
            binding.disagreeOrConfirm.text = getString(R.string.m130_confirm)
            binding.agreeOrCancel.text = getString(R.string.m6_cancel)
        } else {
            binding.title.text = getString(R.string.m21_user_agreement_and_privacy_policy)
            binding.llContent.visible()
            binding.disagreeOrConfirm.text = getString(R.string.m19_disagree)
            binding.agreeOrCancel.text = getString(R.string.m20_agree)
        }
    }

    private fun initView() {
        binding.tvSpan.run {
            highlightColor = resources.getColor(android.R.color.transparent)
            movementMethod = LinkMovementMethod.getInstance()
            text = getTvSpan()
        }
        binding.disagreeOrConfirm.setOnClickListener(this)
        binding.agreeOrCancel.setOnClickListener(this)
    }

    private fun getTvSpan(): SpannableString {
        val userAgreement = getString(R.string.m23_user_agreement)
        val privacyPolicy = getString(R.string.m24_privacy_policy)
        val content = getString(R.string.m25_click_agree, userAgreement, privacyPolicy)
        return SpannableString(content).apply {
            addColorSpan(this, userAgreement)
            addClickSpan(this, userAgreement) {
                showDialog()
//                staticResourceViewModel.fetchStaticResource(ApiPath.StaticResource.USER_AGREEMENT)
            }
            addColorSpan(this, privacyPolicy)
            addClickSpan(this, privacyPolicy) {
                showDialog()
//                staticResourceViewModel.fetchStaticResource(ApiPath.StaticResource.PRIVACY_POLICY)
            }
        }
    }

    private fun addColorSpan(spannable: SpannableString, colorSpanContent: String) {
        val span = ForegroundColorSpan(resources.getColor(R.color.text_red))
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.disagree_or_confirm -> {
                if (isShowTipView) {
                    dismissAllowingStateLoss()
                    UserAgreementMonitor.notify(false)
                } else {
                    isShowTipView = true
                    updateView()
                }

            }
            R.id.agree_or_cancel -> {
                if (isShowTipView) {
                    isShowTipView = false
                    updateView()
                } else {
                    dismissAllowingStateLoss()
                    UserAgreementMonitor.notify(true)
                    WECOApplication.instance().storageService().put(KEY_IS_AGREE_AGREEMENT, true)
                }
            }
        }
    }
}
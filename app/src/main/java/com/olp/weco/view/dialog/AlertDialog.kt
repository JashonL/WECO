package com.olp.weco.view.dialog

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.olp.weco.base.BaseDialogFragment
import com.olp.lib.util.gone
import com.olp.lib.util.visible
import com.olp.weco.databinding.DialogAlertBinding

/**
 * 通用Dialog
 */
class AlertDialog : BaseDialogFragment(), View.OnClickListener {

    companion object {

        fun showDialog(
            fm: FragmentManager,
            content: String?,
            cancelText: String? = null,
            comfirText: String? = null,
            title: String? = null,
            onCancelClick: (() -> Unit)? = null,
            onComfirClick: (() -> Unit)?

        ) {
            val dialog = AlertDialog()
            dialog.content = content
            dialog.title = title
            dialog.cancelText = cancelText
            dialog.comfirText = comfirText
            dialog.onCancelClick = onCancelClick
            dialog.onComfirClick = onComfirClick
            dialog.show(fm, AlertDialog::class.java.name)
        }
    }

    private lateinit var binding: DialogAlertBinding
    private var content: String? = null

    private var cancelText: String? = null
    private var comfirText: String? = null
    private var onCancelClick: (() -> Unit)? = null//确定
    private var onComfirClick: (() -> Unit)? = null//取消

    private var title: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAlertBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        binding.btGray.setOnClickListener(this)
        binding.btRed.setOnClickListener(this)
        if (TextUtils.isEmpty(title)) {
            binding.tvTitle.text = content
            binding.tvContent.gone()
        } else {
            binding.tvTitle.text = title
            binding.tvContent.visible()
            binding.tvContent.text = content
        }
        if (!cancelText.isNullOrEmpty()) {
            binding.btGray.text = cancelText
        }
        if (!comfirText.isNullOrEmpty()) {
            binding.btRed.text = comfirText
        }
    }

    override fun onClick(v: View?) {
        when {
            v === binding.btRed -> {
                dismissAllowingStateLoss()
                onCancelClick?.invoke()
            }
            v === binding.btGray -> {
                dismissAllowingStateLoss()
                onComfirClick?.invoke()
            }
        }
    }
}
package com.olp.weco.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.LinearLayout
import com.olp.weco.R
import com.olp.weco.databinding.EditextComposeBinding
import com.olp.lib.util.gone
import com.olp.lib.util.visible

class EditTextComposeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), OnClickListener {


    private var bingding: EditextComposeBinding

    private var hideString: String
    private var leftIcon: Drawable?


    private var rightShow: Boolean
    private var rightIcon: Drawable?
    private var rightText: String?

    private var rightType: Int
    private var contentType: Int


    private var isPasswrod: Boolean


    //设置  获取值
    private var value: String? = ""

    //高阶函数   函数名 :(参数，参数)-> 返回值
    var onRightClick: ((View) -> Unit)? = null


    init {
        val view = LayoutInflater.from(context).inflate(R.layout.editext_compose, this)
        bingding = EditextComposeBinding.bind(view)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.EditTextComposeView,
            0, 0
        ).apply {
            try {
                leftIcon = getDrawable(R.styleable.EditTextComposeView_compose_left_icon)
                hideString = getString(R.styleable.EditTextComposeView_compose_hide) ?: ""

                rightShow = getBoolean(R.styleable.EditTextComposeView_compose_right_show, false)
                rightIcon = getDrawable(R.styleable.EditTextComposeView_compose_rigth_icon)
                rightText = getString(R.styleable.EditTextComposeView_compose_rigth_text) ?: ""

                rightType = getInt(R.styleable.EditTextComposeView_compose_right_type, 1)
                contentType = getInt(R.styleable.EditTextComposeView_compose_content_type, 1)


                isPasswrod =
                    getBoolean(R.styleable.EditTextComposeView_compose_item_ispassword, false)


            } finally {
                recycle()
            }
        }

        initViews()

    }


    private fun initViews() {
        bingding.ivIcon.setImageDrawable(leftIcon)
        bingding.etContent.hint = hideString
        bingding.tvContent.hint = hideString



        if (rightShow) bingding.flRight.visible() else bingding.flRight.gone()


        if (rightType == 1) {
            bingding.ivRightIcon.gone()
            bingding.tvRightText.visible()
        } else {
            bingding.ivRightIcon.visible()
            bingding.tvRightText.gone()
        }


        if (contentType == 1) {
            bingding.etContent.visible()
            bingding.tvContent.gone()
        } else {
            bingding.etContent.gone()
            bingding.tvContent.visible()
        }



        bingding.ivRightIcon.setImageDrawable(rightIcon)
        bingding.tvRightText.text = rightText


        //密码
        if (isPasswrod) {
            bingding.etContent.inputType = InputType.TYPE_NUMBER_VARIATION_PASSWORD
        }

        bingding.flRight.setOnClickListener(this)
    }


    fun getValue(): String? {
        value = if (contentType == 1) {
            bingding.etContent.text.toString()
        } else {
            bingding.tvContent.text.toString()

        }
        return value
    }


    fun setValue(value: String) {
        this.value = value
        bingding.etContent.setText(value)
        bingding.tvContent.setText(value)
    }



    fun setRightText(value: String) {
        bingding.tvRightText.setText(value)
    }



    fun setOnRightClickListener(rightClick: ((View) -> Unit)?) {
        this.onRightClick = rightClick
    }


    fun setRightEnable(enable: Boolean) {
        bingding.flRight.isEnabled = enable
    }


    override fun onClick(p0: View?) {
        when {
            p0 === bingding.flRight -> {
                onRightClick?.invoke(p0)
            }
        }


    }


}
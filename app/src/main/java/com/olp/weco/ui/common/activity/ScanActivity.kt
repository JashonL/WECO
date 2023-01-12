package com.olp.weco.ui.common.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import com.olp.scan.CaptureHelper
import com.olp.scan.OnCaptureCallback
import com.olp.weco.base.BaseActivity
import com.olp.weco.databinding.ActivityScanBinding
import com.olp.weco.ui.dataloger.AddDataLoggerActivity


/**
 * 扫码Activity
 */
class ScanActivity : BaseActivity(), OnCaptureCallback {

    companion object {

        const val KEY_CODE_TEXT = "key_code_text"
        const val KEY_PLANT_ID = "key_plantid"
        const val KEY_DEVICE_ID = "key_device_id"

        fun start(context: Context?, plantId: String, deviceId: String? = null) {
            context?.startActivity(getIntent(context, plantId, deviceId))
        }

        fun getIntent(context: Context?, plantId: String, deviceId: String?=null): Intent {
            return Intent(context, ScanActivity::class.java).apply {
                putExtra(KEY_PLANT_ID, plantId)
                deviceId?.let {
                    putExtra(KEY_DEVICE_ID, deviceId)
                }
            }
        }


    }

    private lateinit var binding: ActivityScanBinding
    private val helper by lazy {
        CaptureHelper(this, binding.surface, binding.viewfinderView, binding.ivFlash)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initLiseners()
    }

    private fun initLiseners() {
        binding.llScanManual.setOnClickListener {
            //跳转到手动输入那里
            AddDataLoggerActivity.start(this, intent.getStringExtra(KEY_PLANT_ID), "")
        }
    }

    private fun initView() {
        helper.also {
            it.setOnCaptureCallback(this)
            it.onCreate()
            it.vibrate(true)
            it.framingRectRatio(0.625f)
        }
    }

    override fun onResultCallback(result: String?): Boolean {
        result?.also {
            val intent = Intent()
            intent.putExtra(KEY_CODE_TEXT, result)
            setResult(RESULT_OK, intent)
            finish()
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        helper.onResume()
    }

    override fun onPause() {
        super.onPause()
        helper.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        helper.onDestroy()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        helper.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

}
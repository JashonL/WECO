package com.olp.weco.ui.common.fragment

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.olp.weco.BuildConfig
import com.olp.weco.app.WECOApplication
import com.olp.lib.util.ToastUtil
import com.olp.lib.util.Util
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 调试页面
 * 1.切换环境
 */
class DebugDialog : DialogFragment() {

    companion object {
        fun showDialog(fragmentManager: FragmentManager) {
            DebugDialog().show(fragmentManager, DebugDialog::class.java.name)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        //测试服务器
        val testHost = "http://47.243.120.111/"
        //开发本地服务器
        val localHost = "http://20.60.5.236:8089/"
        val hostList = arrayOf(BuildConfig.apiServerUrl, testHost, localHost)
        val checkedItem = hostList.indexOf(WECOApplication.instance().apiService().host())
        builder.setTitle("切换环境")
            .setSingleChoiceItems(arrayOf("正式环境", "测试环境", "本地环境"), checkedItem) { _, index ->
                WECOApplication.instance().apiService().setHost(hostList[index])
                ToastUtil.show("环境切换中")
                lifecycleScope.launch {
                    delay(1000)
                    Util.restartApp(requireActivity())
                }
            }
        return builder.create()
    }
}
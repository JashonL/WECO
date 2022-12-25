package com.olp.weco.ui.account.mine

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.olp.bluetooth.util.util.LocalUtils
import com.olp.weco.R
import com.olp.weco.base.BaseFragment
import com.olp.weco.databinding.LeftContentBinding
import com.olp.weco.ui.account.viewmodel.AccountViewModel
import com.olp.weco.ui.account.viewmodel.VerifyCodeViewModel
import com.olp.weco.ui.manu.activity.SettingActivity
import com.olp.weco.view.dialog.AlertDialog

class LeftMenuFragment : BaseFragment(), OnClickListener {


    lateinit var binding: LeftContentBinding
    private val viewModel: AccountViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LeftContentBinding.inflate(inflater)
        initViews()
        initData()
        setOnClickLiseners()
        return binding.root
    }

    private fun initData() {
        viewModel.logoutLiveData.observe(viewLifecycleOwner) {
            dismissDialog()
            if (it == null) {
                accountService().logout()
                accountService().login(requireActivity())
            } else {
                showResultDialog(it,null,null)
            }
        }
    }

    private fun setOnClickLiseners() {
        binding.ivAvatar.setOnClickListener(this)
        binding.ivExit.setOnClickListener(this)


    }

    private fun initViews() {
        refreshUserProfile()
    }


    private fun refreshUserProfile() {
        Glide.with(this).load(accountService().userAvatar())
            .placeholder(R.drawable.ic_default_avatar)
            .into(binding.ivAvatar)
        binding.tvUsername.text = accountService().user()?.email
    }

    override fun onClick(v: View?) {
        when {
            v === binding.ivAvatar -> {
                ActivityMe.start(context)
            }
            v === binding.ivExit -> {
                logout()
            }
        }
    }



    fun logout(){
        AlertDialog.showDialog(
            childFragmentManager,
            getString(R.string.m131_logout),
            getString(R.string.m101_cancel),
            getString(R.string.m102_comfir),
            getString(R.string.m36_tip),
            null
        ) {
            showDialog()
            viewModel.logout(accountService().user()?.email)
        }


    }




}
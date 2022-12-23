package com.olp.weco.ui.manu.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.olp.weco.BuildConfig
import com.olp.weco.R
import com.olp.weco.base.BaseActivity
import com.olp.weco.crop.CropShape
import com.olp.weco.crop.ImageCrop
import com.olp.weco.databinding.ActivitySettingBinding
import com.olp.weco.ui.common.activity.CountryActivity
import com.olp.weco.ui.common.fragment.RequestPermissionHub
import com.olp.weco.ui.manu.viewmodel.SettingViewModel
import com.olp.weco.utils.AppUtil
import com.olp.weco.view.dialog.InputDialog
import com.olp.weco.view.dialog.OptionsDialog
import com.olp.lib.service.account.IAccountService
import com.olp.lib.util.ActivityBridge
import com.olp.lib.util.ToastUtil
import com.olp.lib.util.Util
import java.io.File


/**
 * 设置页面
 */
class SettingActivity : BaseActivity(), View.OnClickListener,
    IAccountService.OnUserProfileChangeListener {

    companion object {

        fun start(context: Context?) {
            context?.startActivity(Intent(context, SettingActivity::class.java))
        }

    }

    private lateinit var binding: ActivitySettingBinding
    private val viewModel: SettingViewModel by viewModels()



    private var takePictureFile: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
        setListener()
    }

    private fun setListener() {
        binding.ivAvatar.setOnClickListener(this)
        binding.itemUserName.setOnClickListener(this)
        binding.itemPassword.setOnClickListener(this)
        binding.itemCountry.setOnClickListener(this)
        binding.itemInstallationCode.setOnClickListener(this)


        accountService().addUserProfileChangeListener(this)
    }

    private fun initView() {
        refreshUserProfile()
    }

    private fun refreshUserProfile() {
        Glide.with(this).load(accountService().userAvatar())
            .placeholder(R.drawable.ic_default_avatar)
            .into(binding.ivAvatar)

        binding.itemUserName.setSubName(accountService().user()?.email)
        binding.itemCountry.setSubName(accountService().user()?.country)
        binding.itemInstallationCode.setSubName(accountService().user()?.userType)
    }

    private fun initData() {
        viewModel.logoutLiveData.observe(this) {
            dismissDialog()
            if (it == null) {
                accountService().logout()
                accountService().login(this)
                finish()
            } else {
                ToastUtil.show(it)
            }
        }



        viewModel.uploadUserAvatarLiveData.observe(this) {
            dismissDialog()
            ToastUtil.show(it.second)
            if (it.first != null) {
                accountService().saveUserAvatar(it.first)
                refreshUserProfile()
            }
        }




    }

    override fun onClick(v: View?) {
        when {
            v === binding.itemUserName -> {

            }
            v === binding.itemPassword -> ModifyPasswordActivity.start(this)
//            v === binding.itemCountry -> ModifyInstallerNoActivity.start(this)
//            v === binding.itemInstallationCode -> ModifyInstallerNoActivity.start(this)

            v===binding.itemCountry->selectCountry()

            v===binding.itemInstallationCode->{
                val installerCode = accountService().user()?.installerCode
                val title = getString(R.string.m186_installer_code)
                showInstallationDialog(title,installerCode.toString())
            }


            v===binding.ivAvatar->{
                selectPictureMode()
            }


        }
    }




    private fun selectPictureMode() {
        val takeAPicture = getString(R.string.m156_take_photo)
        val fromTheAlbum = getString(R.string.m157_select_album)
        val options =
            arrayOf(takeAPicture, fromTheAlbum)
        OptionsDialog.show(supportFragmentManager, options) {
            when (options[it]) {
                takeAPicture -> takeAPicture()
                fromTheAlbum -> fromTheAlbum()
            }
        }
    }






    /**
     * Android官方说明：Intent(MediaStore.ACTION_IMAGE_CAPTURE) 调用系统相机拍照，不需要申请Camera权限
     * 1.小米手机不申请权限会崩溃，所以都申请权限进行适配
     */
    private fun takeAPicture() {
        RequestPermissionHub.requestPermission(
            supportFragmentManager,
            arrayOf(Manifest.permission.CAMERA)
        ) { isGranted ->
            if (isGranted) {
                ActivityBridge.startActivity(
                    this,
                    Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                        takePictureFile = AppUtil.createImageFile()?.apply {
                            putExtra(
                                MediaStore.EXTRA_OUTPUT,
                                FileProvider.getUriForFile(
                                    this@SettingActivity,
                                    BuildConfig.APPLICATION_ID + ".fileProvider",
                                    this
                                )
                            )
                        }
                    },
                    object :
                        ActivityBridge.OnActivityForResult {
                        override fun onActivityForResult(
                            context: Context?,
                            resultCode: Int,
                            data: Intent?
                        ) {
                            if (resultCode == RESULT_OK) {
                                takePictureFile?.also {
                                    Util.galleryAddPic(it.absolutePath)
                                    cropImage(Uri.fromFile(it))
                                }
                            }
                        }
                    })
            }
        }
    }


    private fun fromTheAlbum() {
        RequestPermissionHub.requestPermission(
            supportFragmentManager,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        ) { isGranted ->
            if (isGranted) {
                ActivityBridge.startActivity(
                    this,
                    Intent(Intent.ACTION_PICK).apply {
                        type = "image/*"
                        data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    },
                    object :
                        ActivityBridge.OnActivityForResult {
                        override fun onActivityForResult(
                            context: Context?,
                            resultCode: Int,
                            data: Intent?
                        ) {
                            if (resultCode == RESULT_OK) {
                                cropImage(data?.data)
                            }
                        }
                    })
            }
        }
    }


    private fun cropImage(imageUri: Uri?) {
        imageUri?.also {
            ImageCrop.activity(it)
                .setCropShape(CropShape.CIRCLE)
                .start(this@SettingActivity)
        }
    }






    private fun showInstallationDialog(title:String,content:String) {
            InputDialog.showDialog(
                supportFragmentManager,
                content,
                getString(R.string.m18_confirm),
                getString(R.string.m16_cancel),
                title
            ) {

            }

    }


    private fun selectCountry() {
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
                        val country = data.getStringExtra(CountryActivity.KEY_AREA) ?: ""
                        binding.itemCountry.setSubName(country)
                    }
                }
            })
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            //剪裁图片回调
            if (requestCode == ImageCrop.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                ImageCrop.getActivityResult(data).uri?.path?.also {
                    showDialog()
                    viewModel.uploadUserAvatar(it)
                }
            }
        }
    }

    override fun onUserProfileChange(account: IAccountService) {
        refreshUserProfile()
    }

    override fun onDestroy() {
        accountService().removeUserProfileChangeListener(this)
        super.onDestroy()
    }
}
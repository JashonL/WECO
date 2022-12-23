package com.olp.weco.ui.station.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.olp.lib.util.*
import com.olp.weco.ui.common.fragment.RequestPermissionHub
import com.olp.weco.BuildConfig
import com.olp.weco.R
import com.olp.weco.base.BaseFragment
import com.olp.weco.databinding.FragmentAddPlant2Binding
import com.olp.weco.model.GeneralItemModel
import com.olp.weco.ui.station.viewmodel.AddPlantViewModel
import com.olp.weco.utils.AppUtil
import com.olp.weco.view.dialog.OptionsDialog
import com.olp.weco.view.dialog.PickerDialog

import java.io.File

/**
 * 时区
 * 组件总功率
 * 电站类型
 * 资金收益
 * 电站图片
 */
class AddPlant2Fragment : BaseFragment(), View.OnClickListener {

    private var _binding: FragmentAddPlant2Binding? = null

    private val binding get() = _binding!!
    private val viewModel: AddPlantViewModel by activityViewModels()

    private var isClickSelectCurrency = false
    private var isClickSelectTimeZone = false
    private var takePictureFile: File? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPlant2Binding.inflate(inflater, container, false)
        initData()
        initView()
        setListener()
        return binding.root
    }

    private fun initData() {
        viewModel.timeZoneLiveData.observe(viewLifecycleOwner) {
            dismissDialog()
            if (it.second == null) {
                if (TextUtils.isEmpty(viewModel.addPlantModel.plantTimeZone)) {
                    //时区为空的时候，设置值
                    viewModel.addPlantModel.formulaMoneyUnitId = it.first?.monetaryUnit
                    refreshCurrencyView()
                    viewModel.addPlantModel.plantTimeZone = it.first?.timezoneList?.get(0)
                    refreshTimezoneView()
                }
                if (isClickSelectTimeZone) {
                    it.first?.getGeneralItem()?.let { timeZoneList -> selectTimeZone(timeZoneList) }
                }
            } else {
                ToastUtil.show(it.second)
            }
            isClickSelectTimeZone = false
        }

        viewModel.currencyListLiveData.observe(viewLifecycleOwner) {
            dismissDialog()
            if (it.second == null) {
                if (isClickSelectCurrency) {
                    selectCurrency(it.first)
                } else {
                    setCurrency(it.first[0].name)
                }
            } else {
                ToastUtil.show(it.second)
            }
            isClickSelectCurrency = false
        }

        if (viewModel.isEditMode) {
            showDialog()
            viewModel.fetchTimeZoneList(viewModel.addPlantModel.country ?: "")
        }
    }

    private fun setListener() {
        binding.tvTimezone.setOnClickListener(this)
        binding.tvCurrency.setOnClickListener(this)
        binding.tvUploadPlantImage.setOnClickListener(this)
        binding.ivPlantImage.setOnClickListener(this)
    }

    private fun initView() {
        if (TextUtils.isEmpty(viewModel.addPlantModel.plantFileService)) {
            binding.ivPlantImage.gone()
            binding.tvUploadPlantImage.visible()
        } else {
            Glide.with(this).load(viewModel.addPlantModel.plantFileService)
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.ivPlantImage)
            binding.ivPlantImage.visible()
            binding.tvUploadPlantImage.gone()
        }
        refreshTimezoneView()
        refreshTotalPower()
        refreshFormulaMoney()
    }

    /**
     * 资金收益
     */
    private fun refreshFormulaMoney() {
        refreshFormulaMoneyView()
        refreshCurrencyView()
    }

    private fun refreshFormulaMoneyView() {
        binding.etElectrovalence.setText(viewModel.addPlantModel.formulaMoney)
        binding.etElectrovalence.setSelection(viewModel.addPlantModel.formulaMoney?.length ?: 0)
    }

    override fun onClick(v: View?) {
        when {
            v === binding.tvTimezone -> {
                if (viewModel.addPlantModel.country.isNullOrEmpty()) {
                    ToastUtil.show(getString(R.string.m73_unselected_country_or_area))
                    return
                }
                val timeZone = viewModel.timeZoneLiveData.value?.first
                if (timeZone?.getGeneralItem().isNullOrEmpty()) {
                    isClickSelectTimeZone = true
                    showDialog()
                    viewModel.fetchTimeZoneList(viewModel.addPlantModel.country ?: "")
                    return
                }
                timeZone?.getGeneralItem()?.let { selectTimeZone(it) }
            }
            v === binding.tvCurrency -> {
                val currencyList = viewModel.currencyListLiveData.value?.first
                if (currencyList.isNullOrEmpty()) {
                    isClickSelectCurrency = true
                    showDialog()
                    viewModel.fetchCurrencyList()
                } else {
                    selectCurrency(currencyList)
                }
            }
            v === binding.tvUploadPlantImage -> {
                selectPictureMode()
            }
            v === binding.ivPlantImage -> {
                selectPictureMode()
            }
        }
    }

    private fun selectCurrency(currencyList: Array<GeneralItemModel>) {
        PickerDialog.show(childFragmentManager, currencyList) {
            setCurrency(currencyList[it].name)
        }
    }

    private fun setCurrency(currency: String) {
        viewModel.addPlantModel.formulaMoneyUnitId = currency
        refreshCurrencyView()
    }

    private fun refreshCurrencyView() {
        binding.tvCurrency.text = viewModel.addPlantModel.formulaMoneyUnitId
    }

    private fun selectTimeZone(timeZones: Array<GeneralItemModel>) {
        PickerDialog.show(childFragmentManager, timeZones) {
            viewModel.addPlantModel.plantTimeZone = timeZones[it].name
            refreshTimezoneView()
        }
    }

    private fun refreshTotalPower() {
        binding.etTotalComponentPower.setText(viewModel.addPlantModel.totalPower)
        binding.etTotalComponentPower.setSelection(viewModel.addPlantModel.totalPower?.length ?: 0)
    }

    private fun refreshTimezoneView() {
        binding.tvTimezone.text = viewModel.addPlantModel.plantTimeZone
    }

    private fun selectPictureMode() {
        val takeAPicture = getString(R.string.m74_take_a_picture)
        val fromTheAlbum = getString(R.string.m75_from_the_album)
        val options = arrayOf(takeAPicture, fromTheAlbum)
        OptionsDialog.show(childFragmentManager, options) {
            when (options[it]) {
                takeAPicture -> takeAPicture()
                fromTheAlbum -> fromTheAlbum()
            }
        }
    }

    private fun fromTheAlbum() {
        RequestPermissionHub.requestPermission(
            childFragmentManager,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        ) { isGranted ->
            if (isGranted) {
                ActivityBridge.startActivity(
                    requireActivity(),
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
                            if (resultCode == AppCompatActivity.RESULT_OK && data != null) {
                                setPlantImage(data.data)
                            }
                        }
                    })
            }
        }
    }

    /**
     * 设置圆角与CenterCrop，需要在代码中进行设置，还需要注意顺序
     */
    private fun setPlantImage(file: Uri?) {
        //设置选中的电站图片，同时清空已压缩图片字段
        viewModel.addPlantModel.plantFile = file
        viewModel.addPlantModel.plantFileCompress = null
        Glide.with(this).load(file)
            .apply(
                RequestOptions().transform(
                    CenterCrop(), RoundedCorners(ViewUtil.dp2px(requireContext(), 2f))
                )
            )
            .into(binding.ivPlantImage)
        binding.ivPlantImage.visible()
        binding.tvUploadPlantImage.gone()
    }

    /**
     * Android官方说明：Intent(MediaStore.ACTION_IMAGE_CAPTURE) 调用系统相机拍照，不需要申请Camera权限
     * 1.小米手机不申请权限会崩溃，所以都申请权限进行适配
     */
    private fun takeAPicture() {
        RequestPermissionHub.requestPermission(
            childFragmentManager,
            arrayOf(Manifest.permission.CAMERA)
        ) { isGranted ->
            if (isGranted) {
                ActivityBridge.startActivity(
                    requireActivity(),
                    Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                        takePictureFile = AppUtil.createImageFile()?.apply {
                            putExtra(
                                MediaStore.EXTRA_OUTPUT,
                                FileProvider.getUriForFile(
                                    requireContext(),
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
                            if (resultCode == AppCompatActivity.RESULT_OK) {
                                takePictureFile?.also {
                                    Util.galleryAddPic(it.absolutePath)
                                    setPlantImage(AppUtil.fileToUri(requireActivity(), it))
                                }
                            }
                        }
                    })
            }
        }
    }

    /**
     * 保存输入框的内容
     */
    fun saveEditTextString() {
        viewModel.addPlantModel.totalPower = binding.etTotalComponentPower.text.toString().trim()
        viewModel.addPlantModel.formulaMoney = binding.etElectrovalence.text.toString().trim()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
package com.olp.weco.ui.station.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.olp.weco.ui.common.fragment.RequestPermissionHub
import com.olp.weco.ui.station.monitor.PlantMonitor
import com.olp.lib.util.*
import com.olp.weco.BuildConfig
import com.olp.weco.R
import com.olp.weco.base.BaseActivity
import com.olp.weco.crop.BitmapUtils
import com.olp.weco.databinding.ActivityAddTechPlantBinding
import com.olp.weco.model.*
import com.olp.weco.ui.common.activity.CountryActivity
import com.olp.weco.ui.station.viewmodel.AddPlantViewModel
import com.olp.weco.utils.AppUtil
import com.olp.weco.view.dialog.OptionsDialog
import com.olp.weco.view.dialog.PickerDialog
import com.olp.lib.view.dialog.DatePickerFragment
import com.olp.lib.view.dialog.OnDateSetListener
import com.olp.lib.view.dialog.SelectDateType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class AddTtchPlantActivity : BaseActivity(), View.OnClickListener {

    companion object {

        private const val KEY_PLANT_INFO = "KEY_PLANT_INFO"

        fun start(context: Context?, plantModel: PlantModel? = null) {
            context?.startActivity(Intent(context, AddTtchPlantActivity::class.java).apply {
                if (plantModel != null) {
                    putExtra(KEY_PLANT_INFO, plantModel)
                }
            })
        }

    }

    private lateinit var binding: ActivityAddTechPlantBinding
    private val viewModel: AddPlantViewModel by viewModels()


    private var takePictureFile: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTechPlantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

        initData()
        initlistener()
    }


    private fun initViews() {
        //默认用户国家
        binding.tvCountry.text = accountService().user()?.country

    }

    private fun initlistener() {
        binding.llPic.setOnClickListener(this)
        binding.llStationType.setOnClickListener(this)
        binding.llInstalltionDate.setOnClickListener(this)
        binding.tvCountry.setOnClickListener(this)
        binding.tvCity.setOnClickListener(this)
        binding.btnAdd.setOnClickListener(this)
        binding.llFoundRevenge.setOnClickListener(this)

    }


    private fun initData() {
        viewModel.addPlantLiveData.observe(this) {
            dismissDialog()
            if (it.second == null) {
                PlantMonitor.notifyPlant()
                finish()
            } else {
                ToastUtil.show(it.second)
            }
        }

        viewModel.editPlantLiveData.observe(this) {
            dismissDialog()
            if (it == null) {
                PlantMonitor.notifyPlant()
                finish()
            } else {
                ToastUtil.show(it)
            }
        }

        if (intent.hasExtra(KEY_PLANT_INFO)) {
            viewModel.addPlantModel =
                intent.getParcelableExtra<PlantModel>(KEY_PLANT_INFO)?.convert() ?: AddPlantModel()
            viewModel.isEditMode = true
        } else {
            viewModel.isEditMode = false
        }


        viewModel.cityListLiveData.observe(this) {
            dismissDialog()
            if (it.second == null) {
//                showProvinceList(it.first)
            } else {
                ToastUtil.show(it.second)
            }
        }



        viewModel.moneyUtilListLiveData.observe(this) {
            dismissDialog()
            if (it != null) {
                selectCurrency(it)
            }

        }


    }


    override fun onClick(v: View?) {
        when (v) {
            binding.llPic -> {
                selectPictureMode()
            }

            binding.llStationType -> {
                showStationType()
            }

            binding.llInstalltionDate -> {
                selectDate()
            }


            binding.tvCountry -> {
                selectCountry()
            }

            binding.tvCity -> {
                selectCity()
            }

            binding.ivClearAddr -> {
                binding.etDetailAddr.setText("")
            }

            binding.btnAdd -> {
                checkInput()
            }

            binding.llFoundRevenge -> {
                //货币
                selectIncomeUnit()
            }


        }

    }


    private fun checkInput() {
        val addPlantModel = viewModel.addPlantModel
        addPlantModel.plantName = binding.etStationName.text.toString()
        addPlantModel.address = binding.etDetailAddr.text.toString()

        when {
            TextUtils.isEmpty(addPlantModel.stationType) -> {
                ToastUtil.show(getString(R.string.m118_please_plant_type))
            }

            TextUtils.isEmpty(addPlantModel.plantName) -> {
                ToastUtil.show(getString(R.string.m70_please_input_plant_name))
            }
            addPlantModel.installDate == null -> {
                ToastUtil.show(getString(R.string.m46_please_select_install_date))
            }
            TextUtils.isEmpty(addPlantModel.country) -> {
                ToastUtil.show(getString(R.string.m69_please_select_country_or_area_2))
            }
            TextUtils.isEmpty(addPlantModel.city) -> {
                ToastUtil.show(getString(R.string.m116_please_select_city))
            }
            TextUtils.isEmpty(addPlantModel.address) -> {
                ToastUtil.show(getString(R.string.m119_please_plant_address))
            }
            TextUtils.isEmpty(addPlantModel.formulaMoneyUnitId) -> {
                ToastUtil.show(getString(R.string.m117_please_select_symbol))
            }

            else -> {
                requestAddPlant(addPlantModel)
            }
        }
    }

    private fun requestAddPlant(addPlantModel: AddPlantModel) {
        showDialog()
        if (TextUtils.isEmpty(addPlantModel.plantFileCompress) && addPlantModel.plantFile != null
        ) {
            //执行图片压缩，压缩完成后上传到服务端
            lifecycleScope.launch {
                val async = async(Dispatchers.IO) {
                    val bitmapSampled: BitmapUtils.BitmapSampled =
                        BitmapUtils.decodeSampledBitmap(
                            this@AddTtchPlantActivity,
                            addPlantModel.plantFile,
                            640,
                            640
                        )
                    val compressImagePath = AppUtil.saveBitmapToDisk(
                        this@AddTtchPlantActivity,
                        bitmapSampled.bitmap
                    )
                    compressImagePath
                }
                addPlantModel.plantFileCompress = async.await()
                startRequest()
            }
        } else {
            startRequest()
        }
    }

    private fun startRequest() {
        viewModel.addPlant()
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
                    if (resultCode == RESULT_OK && data?.hasExtra(
                            CountryActivity.KEY_AREA
                        ) == true
                    ) {
                        viewModel.addPlantModel.country =
                            data.getStringExtra(CountryActivity.KEY_AREA) ?: ""
                        refreshSelectAreaView()
                        viewModel.addPlantModel.city = ""
                        refreshSelectCityView()
                        viewModel.cityListLiveData.value = Pair(emptyArray(), null)
                        showDialog()
                        viewModel.fetchCityList(viewModel.addPlantModel.country ?: "")
                    }
                }
            })
    }


    /**
     * 刷新国家或者地区
     */
    private fun refreshSelectAreaView() {
        binding.tvCountry.text = viewModel.addPlantModel.country
    }


    private fun selectIncomeUnit() {
        val currencyList = viewModel.moneyUtilListLiveData.value
        if (currencyList.isNullOrEmpty()) {
            showDialog()
            viewModel.fetchCurrencyList()
        } else {
            selectCurrency(currencyList)
        }

    }


    private fun selectCurrency(currencyList: Array<CurrencyModel>) {
        PickerDialog.show(supportFragmentManager, currencyList) {
            viewModel.addPlantModel.formulaMoneyUnitId = currencyList[it].id.toString()
            binding.tvCurrency.text = currencyList[it].incomeUnit
        }
    }


    private fun selectCity() {
        if (viewModel.addPlantModel.country.isNullOrEmpty()) {
            ToastUtil.show(getString(R.string.m69_please_select_country_or_area_2))
        } else {
            val provinceList = viewModel.cityListLiveData.value?.first
            if (provinceList.isNullOrEmpty()) {
                showDialog()
                viewModel.fetchCityList(viewModel.addPlantModel.country!!)
            } else {
                showProvinceList(provinceList)
            }
        }
    }


    private fun showProvinceList(cityList: Array<CityModel>?) {
        if (cityList == null || cityList.isEmpty()) return
        PickerDialog.show(
            supportFragmentManager,
            cityList
        ) { cityIndex ->
            viewModel.addPlantModel.city = cityList[cityIndex].city
            refreshSelectCityView()
        }
    }


    private fun refreshSelectCityView() {
        binding.tvCity.text = viewModel.addPlantModel.city
    }


    private fun selectDate() {
        DatePickerFragment.show(
            supportFragmentManager,
            System.currentTimeMillis(),
            SelectDateType.DAY,
            object : OnDateSetListener {
                override fun onDateSet(date: Date) {
                    viewModel.addPlantModel.installDate = date
                    binding.tvDate.text = DateUtils.yyyy_MM_dd_format(date)
                }
            })
    }


    private fun showStationType() {

        val stationTypeList = mutableListOf(
            getString(R.string.m113_pv),
            getString(R.string.m114_storage)
        )

        val models = GeneralItemModel.convert(stationTypeList.toTypedArray())
        PickerDialog.show(supportFragmentManager, models) {
            binding.tvStationType.text = models[it].name
            viewModel.addPlantModel.stationType = models[it].name
        }

    }


    private fun selectPictureMode() {
        val takeAPicture = getString(R.string.m74_take_a_picture)
        val fromTheAlbum = getString(R.string.m75_from_the_album)
        val options = arrayOf(takeAPicture, fromTheAlbum)
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
                                    this@AddTtchPlantActivity,
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
                                    setPlantImage(AppUtil.fileToUri(this@AddTtchPlantActivity, it))
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
                            if (resultCode == RESULT_OK && data != null) {
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
                    CenterCrop(), RoundedCorners(ViewUtil.dp2px(this, 2f))
                )
            )
            .into(binding.ivPlantImage)
        binding.ivPlantImage.visible()
    }


}

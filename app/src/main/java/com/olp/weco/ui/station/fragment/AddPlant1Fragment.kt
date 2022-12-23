package com.olp.weco.ui.station.fragment

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.olp.weco.ui.common.fragment.RequestPermissionHub
import com.olp.weco.R
import com.olp.weco.base.BaseFragment
import com.olp.weco.databinding.FragmentAddPlant1Binding
import com.olp.weco.model.ProvinceModel
import com.olp.weco.ui.station.viewmodel.AddPlantViewModel
import com.olp.weco.view.dialog.PickerDialog
import com.olp.lib.service.location.LocationInfo
import com.olp.lib.service.location.OnLocationListener
import com.olp.lib.util.ToastUtil
import com.olp.lib.util.Util
import com.olp.lib.view.dialog.DatePickerFragment
import com.olp.lib.view.dialog.OnDateSetListener
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*

/**
 * 电站名称
 * 安装日期
 * 电站地址
 */
class AddPlant1Fragment : BaseFragment(), View.OnClickListener, OnLocationListener {

    private var _binding: FragmentAddPlant1Binding? = null

    private val binding get() = _binding!!

    private val viewModel: AddPlantViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPlant1Binding.inflate(inflater, container, false)
        initData()
        initView()
        setListener()
        return binding.root
    }

    private fun initData() {
        viewModel.cityListLiveData.observe(viewLifecycleOwner) {
            dismissDialog()
            if (it.second == null) {
            } else {
                ToastUtil.show(it.second)
            }
        }
    }

    private fun showProvinceList(provinceList: Array<ProvinceModel>?) {
        if (provinceList.isNullOrEmpty()) {
            return
        }
        if (provinceList.size == 1) {
            val cityList = provinceList[0].citys
            if (!cityList.isNullOrEmpty()) {
                PickerDialog.show(childFragmentManager, cityList) {
                    viewModel.addPlantModel.city = cityList[it].city
                    refreshSelectCityView()
                }
            }
        } else {
            PickerDialog.show(childFragmentManager, provinceList) { provinceIndex ->
                val cityList = provinceList[provinceIndex].citys
                if (cityList.isNullOrEmpty()) {
                    viewModel.addPlantModel.city = provinceList[provinceIndex].name
                    refreshSelectCityView()
                } else {
                    PickerDialog.show(
                        childFragmentManager,
                        cityList
                    ) { cityIndex ->
                        viewModel.addPlantModel.city = cityList[cityIndex].city
                        refreshSelectCityView()
                    }
                }
            }
        }
    }

    private fun setListener() {
        binding.tvInstallDate.setOnClickListener(this)
        binding.tvMapForChoosing.setOnClickListener(this)
        binding.tvAutoFetch.setOnClickListener(this)
        binding.tvSelectArea.setOnClickListener(this)
        binding.tvSelectCity.setOnClickListener(this)
    }

    private fun initView() {
        refreshPlantName()
        refreshInstallerDateView()
        refreshSelectAreaView()
        refreshLocationView()

    }

    private fun refreshPlantName() {
        binding.etPlantName.setText(viewModel.addPlantModel.plantName)
        binding.etPlantName.setSelection(viewModel.addPlantModel.plantName?.length ?: 0)
    }

    override fun onClick(v: View?) {
        when {
            v === binding.tvInstallDate -> {
                selectDate()
            }
            v === binding.tvMapForChoosing -> {
                fetchPlantAddressModeViewChange(v)
                requestLocationPermission {

                }
            }
            v === binding.tvAutoFetch -> {
                fetchPlantAddressModeViewChange(v)
                requestLocationPermission {
                    showDialog()
                    locationService().addLocationListener(this@AddPlant1Fragment)
                    locationService().requestLocation()
                }
            }
            v === binding.tvSelectArea -> {
                selectArea()
            }
            v === binding.tvSelectCity -> {
                selectCity()
            }
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
            }
        }
    }

    private fun selectArea() {
    }

    private fun fetchPlantAddressModeViewChange(v: View) {
        binding.tvAutoFetch.setTextColor(
            resources.getColor(
                if (v === binding.tvAutoFetch) R.color.text_red else R.color.text_gray_99
            )
        )
        binding.tvMapForChoosing.setTextColor(
            resources.getColor(
                if (v === binding.tvMapForChoosing) R.color.text_red else R.color.text_gray_99
            )
        )
    }

    /**
     * 请求定位权限
     */
    private fun requestLocationPermission(onSuccess: () -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            if (isSystemLocationEnable()) {
                RequestPermissionHub.requestPermission(
                    childFragmentManager,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                ) {
                    if (it) {
                        onSuccess.invoke()
                    }
                }
            }
        }
    }

    /**
     * 判断位置服务是否开启
     */
    private suspend fun isSystemLocationEnable(): Boolean =
        suspendCancellableCoroutine { continuation ->
            if (Util.isSystemLocationEnable()) {
                continuation.resumeWith(Result.success(true))
            } else {

            }
        }

    private fun selectDate() {
        DatePickerFragment.show(childFragmentManager, object : OnDateSetListener {
            override fun onDateSet(date: Date) {
                viewModel.addPlantModel.installDate = date
                refreshInstallerDateView()
            }
        })
    }

    /**
     * 保存输入框的内容
     */
    fun saveEditTextString() {
        viewModel.addPlantModel.plantName = binding.etPlantName.text.toString().trim()
        viewModel.addPlantModel.plantAddress = binding.etDetailAddress.text.toString().trim()
    }

    private fun refreshLocationView() {
        refreshSelectCityView()
        refreshDetailAddressView()
    }

    private fun refreshSelectCityView() {
        binding.tvSelectCity.text = viewModel.addPlantModel.city
    }

    /**
     * 刷新国家或者地区
     */
    private fun refreshSelectAreaView() {
        binding.tvSelectArea.text = viewModel.addPlantModel.country
    }

    private fun refreshDetailAddressView() {
        binding.etDetailAddress.setText(viewModel.addPlantModel.plantAddress)
        binding.etDetailAddress.setSelection(binding.etDetailAddress.length())
    }

    private fun refreshInstallerDateView() {
        binding.tvInstallDate.text = viewModel.addPlantModel.getDateString()
    }

    override fun locationSuccess(locationInfo: LocationInfo) {
        dismissDialog()
        viewModel.addPlantModel.city = locationInfo.city
        viewModel.addPlantModel.plantAddress = locationInfo.address
        refreshLocationView()
    }

    override fun locationFailure(errorMsg: String) {
        dismissDialog()
        ToastUtil.show(errorMsg)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        locationService().removeLocationListener(this)
        _binding = null
    }

}
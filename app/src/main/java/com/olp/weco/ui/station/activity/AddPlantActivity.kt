package com.olp.weco.ui.station.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.olp.weco.ui.station.fragment.AddPlant2Fragment
import com.olp.weco.ui.station.monitor.PlantMonitor
import com.olp.weco.R
import com.olp.weco.app.WECOApplication
import com.olp.weco.base.BaseActivity
import com.olp.weco.databinding.ActivityAddPlantBinding
import com.olp.weco.model.AddPlantModel
import com.olp.weco.model.PlantModel
import com.olp.weco.ui.station.fragment.AddPlant1Fragment
import com.olp.weco.ui.station.viewmodel.AddPlantViewModel
import com.olp.lib.util.ToastUtil

import kotlinx.coroutines.launch

/**
 * 添加电站页面
 * @see com.growatt.atess.ui.plant.fragment.AddPlant1Fragment
 * @see com.growatt.atess.ui.plant.fragment.AddPlant2Fragment
 */
class AddPlantActivity : BaseActivity(), View.OnClickListener {

    companion object {

        private const val KEY_PLANT_INFO = "KEY_PLANT_INFO"

        fun start(context: Context?, plantModel: PlantModel? = null) {
            if (WECOApplication.instance().accountService().isGuest()) {
                ToastUtil.show(
                    WECOApplication.instance().getString(R.string.m66_info_space_not_permission)
                )
            } else {
                context?.startActivity(Intent(context, AddPlantActivity::class.java).apply {
                    if (plantModel != null) {
                        putExtra(KEY_PLANT_INFO, plantModel)
                    }
                })
            }

        }

    }

    private lateinit var addPlant1Fragment: AddPlant1Fragment
    private lateinit var addPlant2Fragment: AddPlant2Fragment

    private lateinit var binding: ActivityAddPlantBinding

    private val viewModel: AddPlantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
        setListener()
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
    }

    private fun setListener() {
        binding.btNextStep.setOnClickListener(this)
    }

    private fun initView() {


        addPlant1Fragment =
            supportFragmentManager.findFragmentById(R.id.fragment_add_plant_1) as AddPlant1Fragment
        addPlant2Fragment =
            supportFragmentManager.findFragmentById(R.id.fragment_add_plant_2) as AddPlant2Fragment
    }

    override fun onClick(v: View?) {
        when {
            v === binding.btNextStep -> {
                addPlant1Fragment.saveEditTextString()
                addPlant2Fragment.saveEditTextString()
                checkInput()
            }
        }
    }

    private fun checkInput() {
        val addPlantModel = viewModel.addPlantModel

    }

    private fun requestAddPlant(addPlantModel: AddPlantModel) {
        showDialog()
        if (TextUtils.isEmpty(addPlantModel.plantFileCompress) && addPlantModel.plantFile != null
        ) {
            //执行图片压缩，压缩完成后上传到服务端
            lifecycleScope.launch {

            }
        } else {
            startRequest()
        }
    }

    private fun startRequest() {
        if (viewModel.isEditMode) {
            viewModel.editPlant()
        } else {
            viewModel.addPlant()
        }
    }

}
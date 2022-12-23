package com.olp.weco.ui.station.viewmodel

import com.olp.weco.R
import com.olp.weco.app.WECOApplication


data class PlantFilterModel(val filterName: String, val filterType: Int) {

    companion object {

        fun createFilters(): Array<PlantFilterModel> {
            return arrayOf(
                PlantFilterModel(
                    WECOApplication.instance().getString(R.string.m71_install_date), 1
                ), PlantFilterModel(
                    WECOApplication.instance().getString(R.string.m76_device_count), 2
                ), PlantFilterModel(
                    WECOApplication.instance().getString(R.string.m77_total_component_power), 3
                ), PlantFilterModel(
                    WECOApplication.instance().getString(R.string.m45_current_power), 4
                ), PlantFilterModel(
                    WECOApplication.instance().getString(R.string.m78_today_generate_electricity), 5
                ), PlantFilterModel(
                    WECOApplication.instance().getString(R.string.m80_total_generate_electricity), 6
                )
            )
        }

        fun getDefaultFilter(): PlantFilterModel {
            return PlantFilterModel(
                WECOApplication.instance().getString(R.string.m71_install_date), 1
            )
        }

    }

    override fun equals(other: Any?): Boolean {
        if (other is PlantFilterModel) {
            if (other.filterType == filterType) {
                return true
            }
        }
        return false
    }

    override fun hashCode(): Int {
        var result = filterName.hashCode()
        result = 31 * result + filterType
        return result
    }

}
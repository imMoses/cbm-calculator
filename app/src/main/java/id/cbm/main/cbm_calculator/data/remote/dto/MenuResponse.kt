package id.cbm.main.cbm_calculator.data.remote.dto // ktlint-disable package-name

import com.google.gson.annotations.SerializedName

data class MenuResponse(
    @SerializedName("menu_engineer")
    val menuEngineer: Int? = null,

    @SerializedName("menu_sales")
    val menuSales: Int? = null,
)

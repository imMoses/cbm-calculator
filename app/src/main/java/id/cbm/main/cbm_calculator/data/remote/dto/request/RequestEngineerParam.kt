package id.cbm.main.cbm_calculator.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class RequestEngineerParam(
    @SerializedName("perhitungan_no")
    val perhitunganNo: String? = null,

    @SerializedName("customer_name")
    val customerName: String? = null,

    @SerializedName("project")
    val project: String? = null,

    @SerializedName("sales")
    val sales: String? = null,

    @SerializedName("as_name")
    val asName: String? = null,
)

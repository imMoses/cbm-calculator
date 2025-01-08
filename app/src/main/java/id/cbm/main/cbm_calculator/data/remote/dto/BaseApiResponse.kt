package id.cbm.main.cbm_calculator.data.remote.dto // ktlint-disable package-name

import com.google.gson.annotations.SerializedName

data class BaseApiResponse<T>(
    @SerializedName("success")
    val success: Boolean? = null,
    val code: Int? = null,
    val message: String? = null,
    val data: T? = null,
)

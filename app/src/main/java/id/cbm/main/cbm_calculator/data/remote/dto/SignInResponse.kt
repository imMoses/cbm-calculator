package id.cbm.main.cbm_calculator.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SignInResponse(
    @SerializedName("token")
    val token: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("type_id")
    val typeId: String? = null,

    @SerializedName("type_name")
    val typeName: String? = null,

    @SerializedName("menu")
    val menu: MenuResponse? = null,


)
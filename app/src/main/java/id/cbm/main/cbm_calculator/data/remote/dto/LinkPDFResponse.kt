package id.cbm.main.cbm_calculator.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LinkPDFResponse(
    @SerializedName("url_pdf")
    val urlPdf: String? = null
)
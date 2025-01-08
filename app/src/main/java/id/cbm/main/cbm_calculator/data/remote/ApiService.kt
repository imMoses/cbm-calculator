package id.cbm.main.cbm_calculator.data.remote

import id.cbm.main.cbm_calculator.data.remote.dto.BaseApiResponse
import id.cbm.main.cbm_calculator.data.remote.dto.LinkPDFResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Streaming
import retrofit2.http.Url

// ktlint-disable package-name

interface ApiService {

    @FormUrlEncoded
    @POST("siginin")
    suspend fun postSignIn(): Response<Void>

    @FormUrlEncoded
    @POST("pdf/engineer")
    suspend fun postDownloadPDF(
        @Field("perhitungan_no") perhitunganNo: String?,
        @Field("customer_name") customerName: String?,
        @Field("project") project: String?,
        @Field("sales") sales: String?,
        @Field("as_name") asName: String?,
    ): Response<BaseApiResponse<LinkPDFResponse>>

    @Streaming
    @GET
    suspend fun downloadFile(@Url url: String): Response<ResponseBody>
}

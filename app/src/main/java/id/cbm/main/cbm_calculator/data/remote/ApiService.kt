package id.cbm.main.cbm_calculator.data.remote

import retrofit2.Response
import retrofit2.http.POST

// ktlint-disable package-name

interface ApiService {

    @POST("siginin")
    suspend fun postSignIn(): Response<Void>
}

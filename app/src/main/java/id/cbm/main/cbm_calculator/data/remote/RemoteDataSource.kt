package id.cbm.main.cbm_calculator.data.remote // ktlint-disable package-name

class RemoteDataSource(private val apiService: ApiService) {
    suspend fun postSignIn() = apiService.postSignIn()
}

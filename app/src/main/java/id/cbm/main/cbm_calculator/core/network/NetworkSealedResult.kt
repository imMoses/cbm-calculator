package id.cbm.main.cbm_calculator.core.network // ktlint-disable package-name

sealed class NetworkSealedResult<T>(val status: ApiStatus, val data: T? = null, val message: String? = null) {

    data class Success<T>(val _data: T?) : NetworkSealedResult<T>(status = ApiStatus.SUCCESS, data = _data, message = null)
    data class Error<T>(val err: String) : NetworkSealedResult<T>(status = ApiStatus.ERROR, data = null, message = err)
    data class Loading<T>(val isLoading: Boolean) : NetworkSealedResult<T>(status = ApiStatus.LOADING)
}

enum class ApiStatus {
    SUCCESS,
    ERROR,
    LOADING,
}

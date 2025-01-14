package id.cbm.main.cbm_calculator.data.remote

import android.content.Context
import id.cbm.main.cbm_calculator.core.network.ApiStatus
import id.cbm.main.cbm_calculator.core.network.NetworkSealedResult
import kotlin.reflect.full.memberProperties

class ApiResultHandler<T>(
    private val context: Context,
    private val onLoading: () -> Unit,
    private val onSuccess: (T?) -> Unit,
    private val onFailure: (String?) -> Unit
) {

    fun handleApiResult(result: NetworkSealedResult<T>) {
        when (result.status) {
            ApiStatus.LOADING -> {
                onLoading()
            }
            ApiStatus.SUCCESS -> {
                onSuccess(result.data)
            }
            ApiStatus.ERROR -> {
                onFailure(result.message)
            }
        }
    }

    @Throws(IllegalAccessException::class, ClassCastException::class)
    inline fun <reified T> Any.getField(fieldName: String): T? {
        this::class.memberProperties.forEach { kCallable ->
            if (fieldName == kCallable.name) {
                return kCallable.getter.call(this) as T?
            }
        }
        return null
    }
}
package id.cbm.main.cbm_calculator.data.remote

import android.content.Context
import id.cbm.main.cbm_calculator.core.network.NetworkHandler
import id.cbm.main.cbm_calculator.core.network.NetworkSealedResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import java.lang.Exception

inline fun <reified T> toResultFlow(context: Context, crossinline call: suspend () -> Response<T>): Flow<NetworkSealedResult<T>> {
    return flow {
        val isInternetConnected = NetworkHandler(context).isNetworkAvailable()
        if (isInternetConnected) {
            emit(NetworkSealedResult.Loading(true))
            try {
                val c = call()
                if (c.isSuccessful && c.body() != null) {
                    emit(NetworkSealedResult.Success(c.body()))
                } else {
                    emit(NetworkSealedResult.Error(c.message()))
                }
            } catch (e: Exception) {
                emit(NetworkSealedResult.Error(e.message?.toString() ?: "exception happened"))
            }
        } else {
            emit(NetworkSealedResult.Error("No Internet Connection"))
        }
    }.flowOn(Dispatchers.IO)
}
package id.cbm.main.cbm_calculator.data

import android.content.Context
import id.cbm.main.cbm_calculator.core.network.NetworkSealedResult
import id.cbm.main.cbm_calculator.data.remote.RemoteDataSource
import id.cbm.main.cbm_calculator.data.remote.toResultFlow
import kotlinx.coroutines.flow.Flow

class Repository(private val remoteDataSource: RemoteDataSource) {

    suspend fun signIn(context: Context): Flow<NetworkSealedResult<Void>> {
        return toResultFlow(context) {
            remoteDataSource.postSignIn()
        }
    }
}
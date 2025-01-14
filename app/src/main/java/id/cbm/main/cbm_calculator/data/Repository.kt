package id.cbm.main.cbm_calculator.data // ktlint-disable package-name

import android.content.Context
import id.cbm.main.cbm_calculator.core.network.NetworkSealedResult
import id.cbm.main.cbm_calculator.data.remote.RemoteDataSource
import id.cbm.main.cbm_calculator.data.remote.dto.BaseApiResponse
import id.cbm.main.cbm_calculator.data.remote.dto.LinkPDFResponse
import id.cbm.main.cbm_calculator.data.remote.dto.SignInResponse
import id.cbm.main.cbm_calculator.data.remote.dto.request.RequestEngineerParam
import id.cbm.main.cbm_calculator.data.remote.toResultFlow
import kotlinx.coroutines.flow.Flow

class Repository(private val remoteDataSource: RemoteDataSource) {

    suspend fun signIn(context: Context, email: String?, password: String?): Flow<NetworkSealedResult<BaseApiResponse<SignInResponse>>> {
        return toResultFlow(context) {
            remoteDataSource.postSignIn(email, password)
        }
    }

    suspend fun downloadPdf(context: Context, requestParam: RequestEngineerParam): Flow<NetworkSealedResult<BaseApiResponse<LinkPDFResponse>>> {
        return toResultFlow(context) {
            remoteDataSource.postDownloadPdf(requestParam)
        }
    }
}

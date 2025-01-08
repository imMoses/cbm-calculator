package id.cbm.main.cbm_calculator.data

import android.content.Context
import id.cbm.main.cbm_calculator.core.network.NetworkSealedResult
import id.cbm.main.cbm_calculator.data.remote.RemoteDataSource
import id.cbm.main.cbm_calculator.data.remote.dto.BaseApiResponse
import id.cbm.main.cbm_calculator.data.remote.dto.LinkPDFResponse
import id.cbm.main.cbm_calculator.data.remote.dto.request.RequestEngineerParam
import id.cbm.main.cbm_calculator.data.remote.toResultFlow
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

class Repository(private val remoteDataSource: RemoteDataSource) {

    suspend fun signIn(context: Context): Flow<NetworkSealedResult<Void>> {
        return toResultFlow(context) {
            remoteDataSource.postSignIn()
        }
    }

    suspend fun downloadPdf(context: Context, requestParam: RequestEngineerParam): Flow<NetworkSealedResult<BaseApiResponse<LinkPDFResponse>>> {
        return toResultFlow(context) {
            remoteDataSource.postDownloadPdf(requestParam)
        }
    }

    suspend fun streamingDownloadFilePdf(context: Context, url: String) : Flow<NetworkSealedResult<ResponseBody>> {
        return toResultFlow(context) {
            remoteDataSource.downloadingFilePDF(url)
        }
    }
}
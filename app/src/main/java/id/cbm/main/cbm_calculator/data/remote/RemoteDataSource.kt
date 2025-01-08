package id.cbm.main.cbm_calculator.data.remote

import id.cbm.main.cbm_calculator.data.remote.dto.request.RequestEngineerParam

// ktlint-disable package-name

class RemoteDataSource(private val apiService: ApiService) {
    suspend fun postSignIn() = apiService.postSignIn()
    suspend fun postDownloadPdf(
        param: RequestEngineerParam?,
    ) = apiService.postDownloadPDF(
        perhitunganNo = param?.perhitunganNo,
        customerName = param?.customerName,
        project = param?.project,
        sales = param?.sales,
        asName = param?.asName,
    )

    suspend fun downloadingFilePDF(url: String) = apiService.downloadFile(url)
}

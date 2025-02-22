package id.cbm.main.cbm_calculator.ui.engineer.form // ktlint-disable package-name

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.cbm.main.cbm_calculator.core.network.NetworkSealedResult
import id.cbm.main.cbm_calculator.core.viewmodels.BaseViewModel
import id.cbm.main.cbm_calculator.data.Repository
import id.cbm.main.cbm_calculator.data.remote.dto.BaseApiResponse
import id.cbm.main.cbm_calculator.data.remote.dto.LinkPDFResponse
import id.cbm.main.cbm_calculator.data.remote.dto.request.RequestEngineerParam
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class RequestFormViewModel(private val repository: Repository, application: Application) : BaseViewModel(application) {

    private val _responseDownloadPdfEngineer: MutableLiveData<NetworkSealedResult<BaseApiResponse<LinkPDFResponse>>> = MutableLiveData()
    val responseDownlaodPdfEngineer: LiveData<NetworkSealedResult<BaseApiResponse<LinkPDFResponse>>> = _responseDownloadPdfEngineer

    private val _responseBodyFilePdf: MutableLiveData<NetworkSealedResult<ResponseBody>> = MutableLiveData()
    val responseBodyFilePdf: LiveData<NetworkSealedResult<ResponseBody>> = _responseBodyFilePdf

    // get link downlaod from api
    fun retrieveDownloadPdfRequestEngineer(param: RequestEngineerParam) = viewModelScope.launch {
        repository.downloadPdf(
            context = context,
            requestParam = param,
        ).collect { values ->
            _responseDownloadPdfEngineer.value = values
        }
    }
}

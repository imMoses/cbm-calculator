package id.cbm.main.cbm_calculator.ui.signin // ktlint-disable package-name

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.cbm.main.cbm_calculator.core.network.NetworkSealedResult
import id.cbm.main.cbm_calculator.core.viewmodels.BaseViewModel
import id.cbm.main.cbm_calculator.data.Repository
import id.cbm.main.cbm_calculator.data.remote.dto.BaseApiResponse
import id.cbm.main.cbm_calculator.data.remote.dto.SignInResponse
import kotlinx.coroutines.launch

class SignInViewModel(private val repo: Repository, application: Application) : BaseViewModel(application) {

    private val _signInResponse: MutableLiveData<NetworkSealedResult<BaseApiResponse<SignInResponse>>> = MutableLiveData()
    val signInResponse: LiveData<NetworkSealedResult<BaseApiResponse<SignInResponse>>> = _signInResponse

    fun signInCBM(email: String?, password: String?) = viewModelScope.launch {
        repo.signIn(
            context = context,
            email = email,
            password = password,
        ).collect { values ->
            _signInResponse.value = values
        }
    }
}

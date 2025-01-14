package id.cbm.main.cbm_calculator.ui.signin // ktlint-disable package-name

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import id.cbm.main.cbm_calculator.BuildConfig
import id.cbm.main.cbm_calculator.core.base_ui.BaseActivity
import id.cbm.main.cbm_calculator.core.listener.IPermissionListener
import id.cbm.main.cbm_calculator.data.remote.ApiResultHandler
import id.cbm.main.cbm_calculator.data.remote.dto.BaseApiResponse
import id.cbm.main.cbm_calculator.data.remote.dto.SignInResponse
import id.cbm.main.cbm_calculator.databinding.ActivitySignInBinding
import id.cbm.main.cbm_calculator.ui.mainengineer.MainEngineerActivity
import id.cbm.main.cbm_calculator.utils.Constants
import id.cbm.main.cbm_calculator.utils.DialogAlertHelper
import id.cbm.main.cbm_calculator.utils.PermissionHelper
import id.cbm.main.cbm_calculator.utils.setSafeOnClickListener
import org.koin.android.ext.android.inject
import pub.devrel.easypermissions.AfterPermissionGranted
import java.lang.Exception

class SignInActivity : BaseActivity<ActivitySignInBinding>() {

    private val viewModel: SignInViewModel by inject()

    override fun getViewBinding() = ActivitySignInBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // dummy login
//        if (BuildConfig.DEBUG) {
//            binding.tieEmail.setText("hello@gmail.com")
//            binding.tiePassword.setText("test123")
//        }

        // all menu activated
        if (BuildConfig.DEBUG) {
            binding.tieEmail.setText("admin@admin.com")
            binding.tiePassword.setText("admin")
        }

        // menu engineering only
//        if (BuildConfig.DEBUG) {
//            binding.tieEmail.setText("engineer@cbm.com")
//            binding.tiePassword.setText("admin")
//        }

        // menu hitung cepat terus
//        if (BuildConfig.DEBUG) {
//            binding.tieEmail.setText("sales@cbm.com")
//            binding.tiePassword.setText("admin")
//        }

        initPermissionNetworkState()
        observeSignIn()

        binding.tvAppVersion.text = String.format("v.%s", BuildConfig.VERSION_NAME)
        binding.btnSignIn.setSafeOnClickListener {
            validateSignIn()
        }
    }

    private fun initPermissionNetworkState() {
        PermissionHelper.checkGrantedPermission(
            context = this,
            perms = listOf(
                PermissionHelper.permissionNetworkState(),
            ),
            requestCode = Constants.PERMISSION_NETWORK_STATE,
            listener = object : IPermissionListener {
                override fun onPermissionGranted() {
                    Log.w(SignInActivity::class.simpleName, "permission network access state granted")
                }

                override fun onFailed(requestCode: Int, perms: MutableList<String>) {
                    Log.e(SignInActivity::class.simpleName, "some permission failed - $requestCode")
                    perms.forEach {
                        Log.e(SignInActivity::class.simpleName, "failed permission - $it")
                    }
                    Toast.makeText(this@SignInActivity, "Some permission failed", Toast.LENGTH_SHORT).show()
                }
            },
        )
    }

    fun validateSignIn() {
        binding.apply {
            if (tieEmail.text.isNullOrBlank()) {
                tieEmail.error = "Please input your email"
                return
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(tieEmail.text.toString().trim()).matches()) {
                tieEmail.error = "Email format invalid"
                return
            }

            if (tiePassword.text.isNullOrBlank()) {
                tiePassword.error = "Please input password sign in"
                return
            }

            signIn()
        }
    }

    @AfterPermissionGranted(Constants.PERMISSION_MEDIA_STORAGE_ACCESS)
    fun signIn() {
        PermissionHelper.checkGrantedPermission(
            context = this@SignInActivity,
            perms = listOf(PermissionHelper.permissionMediaAccess()),
            requestCode = Constants.PERMISSION_MEDIA_STORAGE_ACCESS,
            listener = object : IPermissionListener {
                override fun onPermissionGranted() {
                    viewModel.signInCBM(
                        email = binding.tieEmail.text.toString(),
                        password = binding.tiePassword.text.toString(),
                    )
                }

                override fun onFailed(requestCode: Int, perms: MutableList<String>) {
                    Toast.makeText(this@SignInActivity, "Granted Permission MEDIA_ACCESS failed - $requestCode", Toast.LENGTH_SHORT).show()
                }
            },
        )
    }

    private fun observeSignIn() {
        try {
            viewModel.signInResponse.observe(this) { result ->
                val apiResultHandler = ApiResultHandler<BaseApiResponse<SignInResponse>>(
                    context = this,
                    onLoading = {
                        loadingDialog()
                    },
                    onSuccess = {
                        dismissLoading()

                        Log.w(SignInActivity::class.simpleName, "signIn() permission media access state granted")
                        Toast.makeText(this@SignInActivity, "Success", Toast.LENGTH_SHORT).show()
                        startActivity(
                            Intent(this@SignInActivity, MainEngineerActivity::class.java),
                        )
                        finishAffinity()
                    },
                    onFailure = {
                        dismissLoading()
                        DialogAlertHelper.showDialogMessage(
                            context = this,
                            message = it ?: "",
                            title = "Login Gagal!",
                            listener = object : DialogAlertHelper.DialogInfoListener {
                                override fun onClickOk() {
                                    Toast.makeText(this@SignInActivity, "Mohon coba dengan akun sign-in yang lain", Toast.LENGTH_SHORT).show()
                                }
                            },
                        )
                    },
                )

                apiResultHandler.handleApiResult(result)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(SignInActivity::class.simpleName, "err observeeSignIn: ${e.message}")
        }
    }
}

package id.cbm.main.cbm_calculator.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import id.cbm.main.cbm_calculator.core.base_ui.BaseActivity
import id.cbm.main.cbm_calculator.databinding.ActivityWebViewBinding
import java.lang.Exception

class WebViewActivity : BaseActivity<ActivityWebViewBinding>() {

    companion object {
        fun initWebView(context: Context?, url: String?): Intent {
            Intent(context, WebViewActivity::class.java).let {
                it.putExtra("url", url)
                return it
            }
        }
    }

    private var mUrl: String? = null

    override fun getViewBinding() = ActivityWebViewBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.appBarMainEngineer.setNavBackListener { finish() }

        mUrl = intent.getStringExtra("url")
        Log.e(WebViewActivity::class.simpleName, "moses_check: url: ${mUrl}")

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.webview, InputMethodManager.SHOW_IMPLICIT)

        binding.webview.let {
            it.settings.loadWithOverviewMode = true
            it.settings.useWideViewPort = true
            it.settings.domStorageEnabled = true
            it.settings.javaScriptEnabled = true
            it.settings.allowContentAccess = true
            it.isFocusableInTouchMode = true
            it.addJavascriptInterface(WebAppInterface(), "NativeAndroid")
            it.webViewClient = object : WebViewClient() {
                override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                    binding.progressBar.visibility = View.GONE
                    if (applicationContext != null) {
                        Toast.makeText(applicationContext, description, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                    binding.progressBar.visibility = View.GONE
                    Log.w("onPageStarted", url)
                }

                override fun onPageFinished(webView: WebView, url: String) {
                    binding.progressBar.visibility = View.GONE
                    Log.w("onPageFinished", url)
                    webView.url
                }

                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    binding.progressBar.visibility = View.GONE
                    Log.e("shouldOverrindin", request?.url.toString())
                    try {
                        packageManager.getPackageInfo(classifiedPackageInfo(request?.url.toString()), PackageManager.GET_ACTIVITIES)
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(request?.url.toString())
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    } catch (e: PackageManager.NameNotFoundException) {
                        Log.w(WebViewActivity::class.simpleName, "PackageManager exception err: ${e.message}")
                        e.printStackTrace()
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(request?.url.toString())
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this@WebViewActivity, "Exception: ${e.message}", Toast.LENGTH_SHORT).show()
//                        FirebaseCrashlyticsHelper.sendCrashReport(
//                            url = "err shouldOverrideUrlLoading webview",
//                            message = e.message ?: "",
//                            others = hashMapOf(
//                                "username" to pref?.username.toString(),
//                                "email" to pref?.email.toString(),
//                                "device" to "${Build.VERSION.SDK_INT}-${Build.MODEL}-${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})"
//                            ),
//                            recordType = "exception shouldOverrideUrlLoading on webview "
//                        )
                    }
                    return true
                }
            }
//            it.evaluateJavascript(
//                "(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();",
//                object : ValueCallback<String>{
//                    override fun onReceiveValue(value: String?) {
//                        Log.e("copyButton", "response: $value")
//                    }
//                }
//            )

            it.loadUrl(mUrl ?: "https://google.com")
        }

    }

    private fun classifiedPackageInfo(url: String?): String {
        /**
         * WA: https://api.whatsapp.com/send/?text=5+Tips+Untuk+Pilihan+Baja+Ringan+https%3A%2F%2Fderatech.id%2Fcbm%2Fnews%2F5-tips-untuk-pilihan-baja-ringan&type=custom_url&app_absent=0
         * Twitter: https://twitter.com/i/flow/login?redirect_after_login=%2Fshare%3Furl%3Dhttps%3A%2F%2Fderatech.id%2Fcbm%2Fnews%2F5-tips-untuk-pilihan-baja-ringan%26text%3D5%2520Tips%2520Untuk%2520Pilihan%2520Baja%2520Ringan
         * FB: https://facebook.com/sharer/sharer.php?u=https://deratech.id/cbm/news/5-tips-untuk-pilihan-baja-ringan
         * LinkedIN: https://www.linkedin.com/shareArticle?url=https://deratech.id/cbm/news/5-tips-untuk-pilihan-baja-ringan&title=5%20Tips%20Untuk%20Pilihan%20Baja%20Ringan
         */
        return if (!url.isNullOrEmpty()) {
            if (url.contains("whatsapp")) {
                "com.whatsapp"
            } else if (url.contains("twitter")) {
                "com.twitter.android"
            } else if (url.contains("facebook")) {
                "com.facebook.katana"
            } else if (url.contains("linkedin")) {
                "com.whatsapp"
            } else {
                ""
            }
        } else {
            ""
        }
    }

    inner class WebAppInterface {

        @JavascriptInterface
        fun copyToClipboard(text: String?) {
//            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//            val clip: ClipData = ClipData.newPlainText("cbm_copy_text", text)
//            clipboard.setPrimaryClip(clip)
//            HelperGeneral.copyClipboard(this@WebViewActivity, text)
        }
    }
}
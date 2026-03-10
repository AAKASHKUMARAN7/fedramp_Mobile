package com.vtmis.fedramp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.vtmis.fedramp.thadam.service.ThadamService
import com.vtmis.fedramp.thadam.ui.OnboardingActivity
import com.vtmis.fedramp.thadam.ui.ThadamDashboardActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Show onboarding on first launch
        if (OnboardingActivity.shouldShow(this)) {
            startActivity(Intent(this, OnboardingActivity::class.java))
        }

        setContentView(R.layout.activity_main)

        // Start Thadam C3 Trust Monitoring Service
        startThadamService()

        webView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.allowFileAccess = true
        @Suppress("DEPRECATION")
        webView.settings.allowFileAccessFromFileURLs = true
        @Suppress("DEPRECATION")
        webView.settings.allowUniversalAccessFromFileURLs = true

        webView.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(msg: ConsoleMessage): Boolean {
                Log.d("WebConsole", "${msg.sourceId()}:${msg.lineNumber()} ${msg.message()}")
                return true
            }
        }

        // Add JavaScript interface so the web dashboard can open the Thadam screen
        webView.addJavascriptInterface(ThadamBridge(), "ThadamBridge")

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url = request?.url?.toString() ?: return false
                if (url == "thadam://dashboard") {
                    openThadamDashboard()
                    return true
                }
                if (url.startsWith("file:///android_asset/")) {
                    return false
                }
                return false
            }
        }

        webView.loadUrl("file:///android_asset/index.html")
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private fun startThadamService() {
        val serviceIntent = Intent(this, ThadamService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }

    private fun openThadamDashboard() {
        startActivity(Intent(this, ThadamDashboardActivity::class.java))
    }

    /** JavaScript bridge so web UI can trigger Thadam dashboard */
    inner class ThadamBridge {
        @android.webkit.JavascriptInterface
        fun openDashboard() {
            runOnUiThread { openThadamDashboard() }
        }
    }
}

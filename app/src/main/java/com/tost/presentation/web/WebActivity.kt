package com.tost.presentation.web

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.tost.databinding.ActivityWebBinding
import java.lang.IllegalArgumentException

/**
 * Created By Malibin
 * on 3월 11, 2021
 */

class WebActivity : AppCompatActivity() {

    private var binding: ActivityWebBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityWebBinding.inflate(layoutInflater).also { binding = it }
        setContentView(binding.root)

        binding.webView.apply {
            webViewClient = MWebViewClient(binding)
            webChromeClient = MWebChromeClient(binding)
            initWebViewSettings(this)
            loadUrl(getWebUrl())
        }
    }

    override fun onBackPressed() {
        if (binding?.webView?.canGoBack() == true) {
            binding?.webView?.goBack()
            return
        }
        super.onBackPressed()
    }

    private fun initWebViewSettings(webView: WebView) {
        webView.settings.apply {
            javaScriptEnabled = true
            allowFileAccess = true
            loadWithOverviewMode = true
            useWideViewPort = true
            setSupportZoom(false)
            builtInZoomControls = false
            layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
            cacheMode = WebSettings.LOAD_NO_CACHE
            domStorageEnabled = true
        }
    }

    private fun getWebUrl() = intent.getStringExtra(WEB_URL_KEY)
        ?: throw IllegalArgumentException("webUrl must be send")

    private class MWebViewClient(private val binding: ActivityWebBinding) : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            binding.progressbar.visibility = View.VISIBLE
        }
    }

    private class MWebChromeClient(private val binding: ActivityWebBinding) : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            binding.progressbar.progress = newProgress
            if (newProgress == 100) {
                binding.progressbar.visibility = View.GONE
            }
        }
    }

    companion object {
        const val WEB_URL_KEY = "WEB_URL_KEY"
    }
}

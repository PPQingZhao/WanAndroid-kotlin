package com.pp.base.browser

import android.app.Application
import android.graphics.Bitmap
import android.net.http.SslError
import android.text.Html
import android.util.Log
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.pp.base.ThemeViewModel

class WebViewModel(app: Application) : ThemeViewModel(app) {

    val mProgress = MutableLiveData(0)
    val mProgressVisibility = MutableLiveData(View.GONE)
    val mTitle = MutableLiveData<String>()
    val webUrl = MutableLiveData<String>()


    fun load(url: String?) {
        webUrl.value = url
    }

    val webChromeClient = object : WebChromeClient() {
        // 网页加载进度
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            mProgress.value = newProgress
//            Log.e("TAG","progress: $newProgress")
        }

        override fun onReceivedTitle(view: WebView?, title: String?) {
            mTitle.value = title
//            Log.e("TAG", "onReceivedTitle: $title")
        }
    }

    val webViewClient = object : WebViewClient() {
        // 使用当前webview打开网页,不调用系统浏览器
        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//            Log.e(WebViewClient1.TAG, url!!)
            webUrl.value = url
            return true
        }

        // 开始载入页面
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            mProgress.value = 0
            mProgressVisibility.value = View.VISIBLE
        }
//                Log.e(TAG, "onPageStarted");

        // 页面加载结束
        override fun onPageFinished(view: WebView?, url: String?) {
            mProgressVisibility.value = View.GONE
            //                Log.e(TAG, "onPageFinished");
        }

        // 加载页面资源,每一个资源的加载都会回调此方法,如加载图片
        override fun onLoadResource(view: WebView?, url: String?) {
//            Log.e(TAG, "onLoadResource")
        }

        // 服务器出错
        override fun onReceivedError(
            view: WebView?,
            errorCode: Int,
            description: String?,
            failingUrl: String?,
        ) {
            mProgress.value = View.GONE
//                Log.e(TAG, "onReceivedError");
        }

        // 处理https请求
        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?,
        ) {
//                Log.e(TAG, "onReceivedSslError");
        }
    }


}
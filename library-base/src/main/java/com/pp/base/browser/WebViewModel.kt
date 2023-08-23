package com.pp.base.browser

import android.app.Application
import android.graphics.Bitmap
import android.net.http.SslError
import android.view.View
import android.webkit.*
import androidx.lifecycle.MutableLiveData
import com.pp.base.ThemeViewModel

class WebViewModel(app: Application) : ThemeViewModel(app) {

    private var mOrignalUrl: String? = null
    val mProgress = MutableLiveData(0)
    val mProgressVisibility = MutableLiveData(View.GONE)
    val mTitle = MutableLiveData<String>()

    fun setUrl(url: String?) {
        mOrignalUrl = url
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
            url?.let {
                view?.loadUrl(it)
            }
            return true
        }

        // 开始载入页面
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            mProgress.value = 0
            mProgressVisibility.value = View.VISIBLE
        }

        // 页面加载结束
        override fun onPageFinished(view: WebView?, url: String?) {
            mProgressVisibility.value = View.GONE
            // mOrignalUrl 是第一个页面,如果webview(复用)已加载过其它页面,需要清除之前页面,确保 mOrignalUrl第一个页面
            if (url == "about:blank" || mOrignalUrl == url) {
                view?.clearHistory()
            }
//            Log.e("TAG", " onPageFinished  $url");
            view?.visibility = View.VISIBLE
        }

        override fun onPageCommitVisible(view: WebView?, url: String?) {
            view?.visibility = View.VISIBLE
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
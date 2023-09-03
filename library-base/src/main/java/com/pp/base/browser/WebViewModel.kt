package com.pp.base.browser

import android.app.Application
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.pp.base.ThemeViewModel

class WebViewModel(app: Application) : ThemeViewModel(app) {
    private var mOrignalUrl: String? = null
    val mProgress = MutableLiveData(0)
    val mProgressVisibility = MutableLiveData(View.GONE)
    val mTitle = MutableLiveData<String>()
    private val mWebView by lazy { WebView(getApplication()) }
    fun getWebView(): WebView {
        return mWebView
    }

    fun setUrl(url: String?) {
        mOrignalUrl = url
        url?.run {
            mWebView.loadUrl(this)
        }
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        initWeb()
    }

    private fun initWeb() {

        mWebView.visibility = View.GONE
        mWebView.clearHistory()

        mWebView.webViewClient = webViewClient
        mWebView.webChromeClient = webChromeClient
        mWebView.settings.let { settings ->

            // 自适应屏幕
            // 将图片调整到适合webview的大小
            settings.useWideViewPort = true
            // 缩放至屏幕大小
            settings.loadWithOverviewMode = true
            // 隐藏缩放控件
            settings.displayZoomControls = false
            // 可缩放
            settings.setSupportZoom(true)
            // 支持手势缩放
            settings.builtInZoomControls = true
            settings.cacheMode =
                WebSettings.LOAD_CACHE_ELSE_NETWORK //只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据
            settings.allowFileAccess = true //设置可以访问文件
            settings.javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口
            settings.loadsImagesAutomatically = true //支持自动加载图片
            settings.defaultTextEncodingName = "utf-8" //设置编码格式

            // h5
            settings.javaScriptEnabled = true

            // dom 缓存
            settings.domStorageEnabled = true

            // 混合模式
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }

            // 最后渲染图片,提高加载速度
//        settings.setBlockNetworkImage(true);
        }
    }

    fun clearWebView() {
        mWebView.let {
            it.clearHistory()
            it.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
        }
    }

    private val webChromeClient = object : WebChromeClient() {
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

    private val webViewClient = object : WebViewClient() {

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
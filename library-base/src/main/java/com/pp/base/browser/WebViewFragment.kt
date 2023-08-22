package com.pp.base.browser

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebSettings
import com.pp.base.ThemeFragment
import com.pp.base.databinding.WebViewBinding
import com.pp.base.databinding.WebViewBindingImpl

open class WebViewFragment : ThemeFragment<WebViewBinding, WebViewModel>() {
    override val mBinding: WebViewBinding by lazy {
        WebViewBindingImpl.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<WebViewModel> {
        return WebViewModel::class.java
    }

    companion object {
        const val WEB_VIEW_TITLE = "title"
        const val WEB_VIEW_URL = "url"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parseArgs()
        enableBackPressed(true)
        initTitle()
        initWeb()
    }

    fun parseArgs() {
        arguments?.let {
            val webUrl = it.getString(WEB_VIEW_URL)
            val webTitle = it.getString(WEB_VIEW_TITLE)

            mViewModel.load(webUrl)
            (if (webTitle?.isNotEmpty() == true) webTitle else "加载中...").let {
//                mBinding.webTvTitle.text = it
                mViewModel.mTitle.value = it
            }
        }
    }

    private fun initTitle() {
        mBinding.webTvTitle.isSelected = true

        mBinding.ivBack.setOnClickListener {
            onBack()
        }
    }

    open fun onBack() {

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWeb() {

        mBinding.webview.webViewClient = mViewModel.webViewClient
        mBinding.webview.webChromeClient = mViewModel.webChromeClient
        mBinding.webview.settings.let { settings ->

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

    fun canGoBack(): Boolean {
        return mBinding.webview.canGoBack()
    }

    fun goBack() {
        mBinding.webview.goBack()
    }

    override fun handleOnBackPressed() {
        if (canGoBack()) {
            goBack()
            return
        }
        enableBackPressed(false)
    }


    override fun onDestroy() {
        super.onDestroy()
        mBinding.webview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
        mBinding.webview.clearHistory()
        (mBinding.webview.parent as ViewGroup).removeView(mBinding.webview)
    }
}
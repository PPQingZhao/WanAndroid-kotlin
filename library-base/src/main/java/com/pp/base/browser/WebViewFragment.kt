package com.pp.base.browser

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.app.SharedElementCallback
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

        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onSharedElementEnd(
                sharedElementNames: MutableList<String>?,
                sharedElements: MutableList<View>?,
                sharedElementSnapshots: MutableList<View>?,
            ) {

                if (isRemoving) {
                    clearWebView()
                }

            }
        })
        super.onCreate(savedInstanceState)

        enableBackPressed(true)
        initTitle()
        initWeb()
        parseArgs()
    }

    fun parseArgs() {
        arguments?.let {
            val webUrl = it.getString(WEB_VIEW_URL)
            val webTitle = it.getString(WEB_VIEW_TITLE)

            mViewModel.setUrl(webUrl)
            (if (webTitle?.isNotEmpty() == true) webTitle else "加载中...").let {
//                mBinding.webTvTitle.text = it
                mViewModel.mTitle.value = Html.fromHtml(webTitle)
            }
        }
    }

    fun initTitle() {
        mBinding.webTvTitle.isSelected = true

        mBinding.ivBack.setOnClickListener {
            onBack()
        }
    }

    open fun onBack() {
        mViewModel.getWebView().let {
            while (it.canGoBack()) {
                it.goBack()
            }
        }
        enableBackPressed(false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun initWeb() {

        mViewModel.getWebView().run {
            if (null != parent) {
                (parent as ViewGroup).removeView(this)
            }
            mBinding.webContainer.addView(
                mViewModel.getWebView(), 0, FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            )
        }
    }

    fun canGoBack(): Boolean {
        return mViewModel.getWebView().canGoBack()
    }

    fun goBack() {
        mViewModel.getWebView().copyBackForwardList().currentItem
        mViewModel.getWebView().goBack()
    }

    override fun handleOnBackPressed() {
        if (canGoBack()) {
            goBack()
        }
        enableBackPressed(canGoBack())
    }

    private fun clearWebView() {
        mViewModel.clearWebView()
        onClearWebView()
    }

    open fun onClearWebView() {

    }

    override fun onDestroy() {
        super.onDestroy()

        if (null == sharedElementEnterTransition) {
            clearWebView()
        }
    }
}
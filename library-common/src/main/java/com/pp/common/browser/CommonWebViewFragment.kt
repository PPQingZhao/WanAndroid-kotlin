package com.pp.common.browser

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.pp.base.browser.WebViewFragment
import com.pp.common.app.App
import com.pp.common.constant.Constants
import com.pp.router_service.RouterPath

class CommonWebViewFragment : WebViewFragment() {

    companion object {
        const val WEB_VIEW_TRANSITION_NAME = "transitionName"
    }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var transitionName: String? = null
        arguments?.let {
            transitionName = it.getString(WEB_VIEW_TRANSITION_NAME)
        }
        mBinding.webTvTitle.transitionName = transitionName

        if (transitionName?.isNotEmpty() == true) {
            allowEnterTransitionOverlap = true
            sharedElementEnterTransition = MaterialContainerTransform().apply {
                duration = Constants.TRANSITION_DURATION
                scrimColor = Color.TRANSPARENT
                postponeEnterTransition()
                var isCreating = true
                mViewModel.mTheme.colorPrimary.observe(this@CommonWebViewFragment) {
                    if (it.defaultColor != 0) {
                        setAllContainerColors(it.defaultColor)
                        if (isCreating) {
                            startPostponedEnterTransition()
                            isCreating = false
                        }
                    }
                }
            }
        } else {
            sharedElementEnterTransition = null
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            parseArgs()
            enableBackPressed(true)
            initTitle()
            initWeb()
        }
    }

    override fun onClearWebView() {

    }

    override fun handleOnBackPressed() {
        super.handleOnBackPressed()
        if (!canGoBack()) {
            App.getInstance().navigation.value = RouterPath.Main.fragment_main to Any()
        }
    }

    override fun onBack() {
        super.goBack()
        App.getInstance().navigation.value = RouterPath.Main.fragment_main to Any()
    }

}
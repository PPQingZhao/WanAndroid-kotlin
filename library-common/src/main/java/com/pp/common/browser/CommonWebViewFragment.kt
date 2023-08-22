package com.pp.common.browser

import android.graphics.Color
import android.os.Bundle
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.pp.base.browser.WebViewFragment
import com.pp.common.app.App
import com.pp.common.constant.Constants
import com.pp.router_service.RouterPath

object CommonWebViewFragment : WebViewFragment() {

        const val WEB_VIEW_TRANSITION_NAME = "transitionName"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val transitionName = it.getString(WEB_VIEW_TRANSITION_NAME)
            mBinding.webTvTitle.transitionName = transitionName
        }

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration = Constants.TRANSITION_DURATION
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(resources.getColor(com.pp.skin.R.color.colorPrimary))
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden){
            parseArgs()
            enableBackPressed(true)
            initTitle()
            initWeb()
        }
    }


    override fun handleOnBackPressed() {
        super.handleOnBackPressed()
        if (!canGoBack()) {
            App.getInstance().navigation.value = RouterPath.Main.fragment_main to Any()
        }
    }

    override fun onBack() {
        App.getInstance().navigation.value = RouterPath.Main.fragment_main to Any()
    }

}
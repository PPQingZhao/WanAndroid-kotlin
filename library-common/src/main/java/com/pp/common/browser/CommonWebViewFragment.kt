package com.pp.common.browser

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.pp.base.browser.WebViewFragment
import com.pp.common.app.App
import com.pp.common.constant.Constants
import com.pp.router_service.RouterPath
import com.pp.theme.getColor

object CommonWebViewFragment : WebViewFragment() {

    const val WEB_VIEW_TRANSITION_NAME = "transitionName"

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var transitionName: String? = null
        arguments?.let {
            transitionName = it.getString(WEB_VIEW_TRANSITION_NAME)
        }
        mBinding.webTvTitle.transitionName = transitionName

        if (transitionName?.isNotEmpty() == true) {
            sharedElementEnterTransition = MaterialContainerTransform().apply {
                duration = Constants.TRANSITION_DURATION
                scrimColor = Color.TRANSPARENT
                if (null != mViewModel.mTheme.colorPrimary.value) {
                    setAllContainerColors(mViewModel.mTheme.colorPrimary.value?.defaultColor!!)
                } else {
                    setAllContainerColors(
                        requireActivity().theme.getColor(
                            android.R.attr.colorPrimary,
                            Color.TRANSPARENT
                        )
                    )
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
package com.pp.common.browser

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.transition.MaterialContainerTransform
import com.pp.base.browser.WebViewFragment
import com.pp.common.constant.TRANSITION_DURATION
import com.pp.common.repository.BrowsingHistoryRepository
import com.pp.common.repository.UserRepository
import com.pp.common.router.MultiRouterFragmentViewModel
import com.pp.router_service.RouterPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Route(path = RouterPath.Web.fragment_web)
class CommonWebViewFragment : WebViewFragment() {

    companion object {
        const val WEB_VIEW_TRANSITION_NAME = "transitionName"
        const val WEB_COLLECT_BROWSING_HISTORY = "collect_browsing_history"
    }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var transitionName: String? = null
        arguments?.let {
            transitionName = it.getString(WEB_VIEW_TRANSITION_NAME)
            val collectHistory = it.getBoolean(WEB_COLLECT_BROWSING_HISTORY, true)

            if (!collectHistory) return@let

            lifecycleScope.launch(Dispatchers.IO) {
                UserRepository.getPreferenceUser()?.userId?.let { userId ->
                    val webUrl = it.getString(WEB_VIEW_URL, "")
                    val webTitle = it.getString(WEB_VIEW_TITLE)
                    BrowsingHistoryRepository.addHistoryWithTime(userId, webTitle, webUrl)
                }
            }
        }
        mBinding.webTvTitle.transitionName = transitionName

        if (transitionName?.isNotEmpty() == true) {
            allowEnterTransitionOverlap = true
            sharedElementEnterTransition = MaterialContainerTransform().apply {
                duration = TRANSITION_DURATION
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

    /*   override fun onHiddenChanged(hidden: Boolean) {
           super.onHiddenChanged(hidden)
           if (!hidden) {
               parseArgs()
               enableBackPressed(true)
               initTitle()
               initWeb()
           }
       }*/

    override fun onClearWebView() {

    }

    override fun onBack() {
        super.onBack()
        MultiRouterFragmentViewModel.popBackStack(mBinding.root, RouterPath.Web.fragment_web)
    }

    override fun handleOnBackPressed() {
        val canGoBack = mViewModel.getWebView().canGoBackOrForward(-1)
        if (!canGoBack) {
            onBack()
        } else {
            super.handleOnBackPressed()
        }
    }

}
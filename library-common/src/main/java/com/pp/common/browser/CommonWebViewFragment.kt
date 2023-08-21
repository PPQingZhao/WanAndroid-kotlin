package com.pp.common.browser

import com.pp.base.browser.WebViewFragment
import com.pp.common.app.App
import com.pp.router_service.RouterPath

class CommonWebViewFragment : WebViewFragment() {

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
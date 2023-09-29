package com.pp.local.ui

import android.app.Application
import android.view.View
import com.pp.base.ThemeViewModel
import com.pp.common.router.MultiRouterFragmentViewModel
import com.pp.router_service.RouterPath

class BrowsingHistoryViewModel(app: Application) : ThemeViewModel(app) {

    fun onBack(view: View) {
        MultiRouterFragmentViewModel.popBackStack(view, RouterPath.Local.fragment_browsing_history)
    }
}
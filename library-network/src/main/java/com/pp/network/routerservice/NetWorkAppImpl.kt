package com.pp.network.routerservice

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.pp.network.cookie.CookieCacheHelper
import com.pp.network.cookie.UserCookieJarImpl
import com.pp.network.utils.HttpUtil
import com.pp.router_service.IAppService
import com.pp.router_service.RouterServiceImpl
import kotlinx.coroutines.Dispatchers

@Route(path = RouterServiceImpl.NetWork.NETWORK_APP)
class NetWorkAppImpl : IAppService() {

    companion object {
        private var sApp: Application? = null
        fun getApp(): Application {
            return checkNotNull(sApp) { "you should init NetWorkAppImpl at first." }
        }
    }

    private val COOKIE_PREFERENCES = "cookie_preferences"
    private val Context.userDataStore by preferencesDataStore(name = COOKIE_PREFERENCES)
    override fun onCreate(application: Application) {
        sApp = application
        HttpUtil.init(
            UserCookieJarImpl(
                CookieCacheHelper(
                    application.userDataStore,
                    ProcessLifecycleOwner.get().lifecycleScope, Dispatchers.IO
                )
            )
        )
    }

}
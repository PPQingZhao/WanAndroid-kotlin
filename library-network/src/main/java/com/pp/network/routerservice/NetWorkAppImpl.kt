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

@Route(path = RouterServiceImpl.NetWork.SERVICE_APP)
class NetWorkAppImpl : IAppService {

    private val COOKIE_PREFERENCES = "cookie_preferences"
    private val Context.userDataStore by preferencesDataStore(name = COOKIE_PREFERENCES)
    override fun onCreate(application: Application) {


        HttpUtil.init(
            UserCookieJarImpl(
                CookieCacheHelper(
                    application.userDataStore,
                    ProcessLifecycleOwner.get().lifecycleScope, Dispatchers.IO
                )
            )
        )
    }

    override fun init(context: Context?) {
    }
}
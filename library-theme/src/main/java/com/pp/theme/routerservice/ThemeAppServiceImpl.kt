package com.pp.theme.routerservice

import android.app.Application
import com.alibaba.android.arouter.facade.annotation.Route
import com.pp.router_service.IAppService
import com.pp.router_service.RouterServiceImpl

/**
 * 监听app 生命周期
 */
@Route(path = RouterServiceImpl.Theme.THEME_APP)
class ThemeAppServiceImpl : IAppService() {

    override fun onCreate(application: Application) {
    }

}
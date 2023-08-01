package com.pp.theme.routerservice

import android.app.Application
import com.alibaba.android.arouter.facade.annotation.Route
import com.pp.router_service.IAppService
import com.pp.router_service.RouterServiceImpl
import com.pp.theme.DynamicThemeManager

/**
 * 监听app 生命周期
 */
@Route(path = RouterServiceImpl.Theme.THEME_APP)
class ThemeAppServiceImpl : IAppService() {

    override fun onCreate(application: Application) {
        // 主题
        DynamicThemeManager.init(application)
    }

}
package com.pp.common.app

import android.app.Application
import android.content.Context
import com.pp.common.theme.WanDynamicTheme
import com.pp.router_service.RouterInitializer
import com.pp.theme.DynamicThemeManager
import kotlin.properties.Delegates

open class App : Application() {

    companion object {
        private var mInstance: App by Delegates.notNull()

        fun getInstance(): App {
            return mInstance
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        mInstance = this

        // 路由初始化
        RouterInitializer.init(this)

        // 动态主题初始化
        DynamicThemeManager
            .init(this@App, WanDynamicTheme.Default)
            .addSkinInfo(WanDynamicTheme.Black)
            .addSkinInfo(WanDynamicTheme.Blue)
    }
}
package com.pp.common.app

import android.app.Application
import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
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


    }

    override fun onCreate() {
        super.onCreate()
        // 动态主题初始化
        DynamicThemeManager
            .init(ProcessLifecycleOwner.get().lifecycleScope, this@App, WanDynamicTheme.Default)
            .addSkinInfo(WanDynamicTheme.Black)
            .addSkinInfo(WanDynamicTheme.Blue)
    }
}
package com.pp.common.app

import android.app.Application
import android.content.Context
import com.pp.router_service.ProcessRouterInitializer
import kotlin.properties.Delegates

open class App : Application() {

    companion object {
        private var mInstance: App by Delegates.notNull()

        fun getInstance(): Application {
            return mInstance
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        mInstance = this

        // 路由初始化
        ProcessRouterInitializer.init(this)
    }

}
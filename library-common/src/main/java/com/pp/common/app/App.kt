package com.pp.common.app

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.pp.router_service.RouterInitializer
import com.pp.router_service.RouterPath
import kotlin.properties.Delegates

open class App : Application() {

    val navigation = MutableLiveData(RouterPath.Main.fragment_main to Any())

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

}
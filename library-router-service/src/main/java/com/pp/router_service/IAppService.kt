package com.pp.router_service

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider
import kotlin.properties.Delegates

/**
 * app 生命周期
 */
abstract class IAppService : IProvider {

    override fun init(context: Context?) {
        // do nothing
    }

    abstract fun onCreate(application: Application)
}
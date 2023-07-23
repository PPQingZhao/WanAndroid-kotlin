package com.pp.module_user.routerservice

import android.app.Application
import android.content.Context
import android.os.UserManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.pp.router_service.IAppService
import com.pp.router_service.RouterServiceImpl
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Route(path = RouterServiceImpl.User.SERVICE_APP)
class UserAppImpl : IAppService {
    override fun onCreate(application: Application) {
        // 登录记录的用户
        GlobalScope.launch {
//            UserManager.loginPreferenceUser()
        }
    }

    override fun init(context: Context?) {
    }
}
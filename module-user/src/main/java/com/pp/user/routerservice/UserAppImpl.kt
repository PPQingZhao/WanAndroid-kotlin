package com.pp.user.routerservice

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.pp.database.AppDataBase
import com.pp.user.manager.UserManager
import com.pp.router_service.IAppService
import com.pp.router_service.RouterServiceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Route(path = RouterServiceImpl.User.USER_APP)
class UserAppImpl : IAppService() {
    override fun onCreate(application: Application) {
        // 登录记录的用户
        ProcessLifecycleOwner.get().apply {
            AppDataBase.instance.observe(this) {
                lifecycleScope.launch(Dispatchers.IO) {
                    UserManager.loginPreferenceUser()
                }
            }
        }
    }

}
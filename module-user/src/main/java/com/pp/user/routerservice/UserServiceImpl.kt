package com.pp.user.routerservice

import android.content.Context
import androidx.lifecycle.LiveData
import com.alibaba.android.arouter.facade.annotation.Route
import com.pp.user.manager.UserManager
import com.pp.router_service.IUserService
import com.pp.router_service.RouterServiceImpl

@Route(path = RouterServiceImpl.User.SERVICE_USER)
class UserServiceImpl : IUserService {

    override fun getToken(): LiveData<String?> {
        return UserManager.getToken()
    }

    override fun getNickName(): LiveData<String?> {
        return UserManager.getNickName()
    }
    override fun getHeadIcon(): LiveData<String?> {
        return UserManager.getHeadIcon()
    }

    override fun getMotto(): LiveData<String?> {
        return UserManager.getMotto()
    }

    override fun init(context: Context?) {
    }
}
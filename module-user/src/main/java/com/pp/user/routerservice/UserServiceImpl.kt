package com.pp.module_user.routerservice

import android.content.Context
import androidx.lifecycle.LiveData
import com.alibaba.android.arouter.facade.annotation.Route
import com.pp.router_service.IUserService
import com.pp.router_service.RouterServiceImpl

@Route(path = RouterServiceImpl.User.SERVICE_USER)
class UserServiceImpl : IUserService {
    override fun getToken(): LiveData<String?> {
        TODO("Not yet implemented")
    }

    override fun getNickName(): LiveData<String?> {
        TODO("Not yet implemented")
    }

    override fun getHeadIcon(): LiveData<String?> {
        TODO("Not yet implemented")
    }

    override fun getMotto(): LiveData<String?> {
        TODO("Not yet implemented")
    }


    override fun init(context: Context?) {
    }
}
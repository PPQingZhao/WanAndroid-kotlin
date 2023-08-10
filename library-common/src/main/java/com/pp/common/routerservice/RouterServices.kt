package com.pp.common.routerservice

import com.alibaba.android.arouter.launcher.ARouter
import com.pp.router_service.IUserService
import com.pp.router_service.RouterServiceImpl

val userService by lazy {
    ARouter.getInstance().build(RouterServiceImpl.User.SERVICE_USER).navigation() as IUserService
}
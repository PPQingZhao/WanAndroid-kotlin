package com.pp.router_service

object RouterServiceImpl {
    /**
     * 数据库
     */
    object DataBase {
        private const val base = "/database_service"
        const val DATABASE_APP = "$base/app"
    }

    /**
     * 用户
     */
    object User{
        private const val base = "/user_service"
        const val USER_APP = "$base/app"
    }

    /**
     * 网络
     */
    object NetWork{
        private const val base = "/network_service"
        const val NETWORK_APP = "$base/app"
    }

    /**
     * 主题
     */
    object Theme{
        private const val base = "/theme_service"
        const val THEME_APP = "$base/app"
    }
}
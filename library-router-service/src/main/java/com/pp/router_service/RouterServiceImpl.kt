package com.pp.router_service

object RouterServiceImpl {
    /**
     * 数据库
     */
    object DataBase {
        private const val base = "/database_service"
        const val DATABASE_APP = "$base/app"
    }

    object User{
        private const val base = "/user_service"
        const val SERVICE_USER = "$base/user"
        const val SERVICE_APP = "$base/app"
    }

    object NetWork{
        private const val base = "/network_service"
        const val SERVICE_APP = "$base/app"
    }
}
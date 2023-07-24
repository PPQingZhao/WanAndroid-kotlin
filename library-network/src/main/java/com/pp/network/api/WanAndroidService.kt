package com.pp.network.api

import com.pp.library_network.api.UserApi
import com.pp.network.utils.RetrofitUtil
import com.pp.network.utils.addHeadersInterceptor
import com.pp.network.utils.bodyLoggingInterceptor

interface WanAndroidService {

    companion object {

        private val header = mutableMapOf<String, String>()
        private const val URL_BASE = "http://chitchat.doujunyu.vip/"
        private val retrofit by lazy {
            RetrofitUtil.create(
                URL_BASE,
                bodyLoggingInterceptor(),
                addHeadersInterceptor(header)
            )
        }

        /**
         * user api
         */
        val userApi: UserApi by lazy { retrofit.create(UserApi::class.java) }

        fun setToken(token: String) {
            header["token"] = token
        }
    }

    object ErrorCode {
        const val FAILED = -1
        const val SUCCESS = 0

        // 登录:账号不存在
        const val LOGIN_USER_INVALID = 1
    }

    object LoginStatus {
        /**
         * 状态:登录
         */
        const val LOGIN = 1
    }
}
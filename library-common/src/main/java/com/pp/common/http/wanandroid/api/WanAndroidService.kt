package com.pp.common.http.wanandroid.api

import com.pp.library_network.api.UserApi
import com.pp.network.utils.RetrofitUtil
import com.pp.network.utils.addHeadersInterceptor
import com.pp.network.utils.bodyLoggingInterceptor

interface WanAndroidService {

    companion object {

        private val header = mutableMapOf<String, String>()
        private const val URL_BASE = "https://www.wanandroid.com/"
        private val retrofit by lazy {
            RetrofitUtil.create(
                URL_BASE,
                bodyLoggingInterceptor(),
                addHeadersInterceptor(header)
            )
        }

        /**
         * api impl
         */
        val userApi: UserApi by lazy { retrofit.create(UserApi::class.java) }
        val homeApi: HomeApi by lazy { retrofit.create(HomeApi::class.java) }

    }

    object ErrorCode {
        const val FAILED = -1
        const val SUCCESS = 0
    }

}
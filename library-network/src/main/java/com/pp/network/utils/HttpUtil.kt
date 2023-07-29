package com.pp.network.utils

import com.pp.network.cookie.UserCookieJarImpl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object HttpUtil {

    private lateinit var userCookieJarImpl: UserCookieJarImpl

    fun init(cookieJarImpl: UserCookieJarImpl) {
        userCookieJarImpl = cookieJarImpl
    }

    fun getClient(vararg interceptor: Interceptor): OkHttpClient {
        val okHttpBuilder = OkHttpClient.Builder()

        interceptor.onEach {
            okHttpBuilder.addInterceptor(it)
        }

        return okHttpBuilder
            .cookieJar(userCookieJarImpl)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()
    }
}
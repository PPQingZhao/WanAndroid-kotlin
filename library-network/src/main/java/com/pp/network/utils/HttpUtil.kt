package com.pp.network.utils

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object HttpUtil {

    fun getClient(vararg interceptor: Interceptor): OkHttpClient {
        val okHttpBuilder = OkHttpClient.Builder()

        interceptor.onEach {
            okHttpBuilder.addInterceptor(it)
        }

        return okHttpBuilder
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()
    }
}
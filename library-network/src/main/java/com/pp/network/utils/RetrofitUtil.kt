package com.pp.network.utils

import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitUtil {

    fun create(
        baseUrl: String,
        vararg interceptor: Interceptor,
    ): Retrofit {
        return Retrofit.Builder()
            .client(HttpUtil.getClient(*interceptor))
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


}
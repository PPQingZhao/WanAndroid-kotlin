package com.pp.network.utils

import android.util.Log
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

private val TAG = "HttpInterceptor"
fun bodyLoggingInterceptor(): HttpLoggingInterceptor {
    val logger = HttpLoggingInterceptor.Logger { message ->
        Log.v(TAG, message)
    }

    val logInterceptor = HttpLoggingInterceptor(logger)
    logInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return logInterceptor
}

fun addHeadersInterceptor( headers: Map<String, String>):Interceptor {
   return Interceptor { chain ->
        val original = chain.request()
        val builder = original.newBuilder()
            .method(original.method(), original.body())

        headers.onEach {
            builder.header(it.key, it.value)
        }

        chain.proceed(builder.build())
    }
}

fun addQueryParameterInterceptor( queryParameter: Map<String, String>):Interceptor {
    return Interceptor { chain ->
        val original = chain.request()

        val urlBuilder = original.url().newBuilder()
        queryParameter.forEach {
            urlBuilder.addQueryParameter(it.key, it.value)
        }

        urlBuilder.host(original.url().host())
            .scheme(original.url().scheme())
        val httpUrl = urlBuilder.build()


        val builder = original.newBuilder()
            .method(original.method(), original.body())
        builder.url(httpUrl)

        chain.proceed(builder.build())
    }
}
package com.pp.common.http.wanandroid.bean

data class ResponseBean<Data>(
    val errorCode: Int,
    val `data`: Data?,
    val errorMsg: String,
)
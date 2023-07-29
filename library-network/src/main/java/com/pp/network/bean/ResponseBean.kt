package com.pp.network.bean

data class ResponseBean<Data>(
    val errorCode: Int,
    val `data`: Data?,
    val errorMsg: String,
)
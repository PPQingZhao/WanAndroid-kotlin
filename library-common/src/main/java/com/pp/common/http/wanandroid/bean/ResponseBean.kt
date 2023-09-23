package com.pp.common.http.wanandroid.bean

data class ResponseBean<Data>(
    val errorCode: Int,
    val `data`: Data?,
    val errorMsg: String?,
)

suspend fun <Data> runCatchingResponse(block: suspend () -> ResponseBean<Data>): ResponseBean<Data> {
    return runCatching {
        block.invoke()
    }.getOrElse {
        ResponseBean(-1, null, it.message)
    }
}
package com.pp.common.http.wanandroid.bean

import com.pp.ui.R
import com.pp.common.app.App
import com.pp.common.http.wanandroid.api.WanAndroidService
import java.net.UnknownHostException

data class ResponseBean<Data>(
    val errorCode: Int,
    val `data`: Data?,
    val errorMsg: String?,
)

suspend fun <Data> runCatchingResponse(block: suspend () -> ResponseBean<Data>): ResponseBean<Data> {
    return runCatching {
        block.invoke()
    }.getOrElse {
        if (it is UnknownHostException) {
            ResponseBean(
                WanAndroidService.ErrorCode.UNKNOWN_HOST, null, App.getInstance().getString(
                    R.string.error_network
                )
            )
        } else {
            ResponseBean(
                WanAndroidService.ErrorCode.UNKNOWN_ERROR, null, App.getInstance().getString(
                    R.string.error_unknown
                )
            )
        }
    }
}
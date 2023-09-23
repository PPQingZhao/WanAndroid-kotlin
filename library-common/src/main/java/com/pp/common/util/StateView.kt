package com.pp.common.util

import com.pp.common.http.wanandroid.api.WanAndroidService
import com.pp.common.http.wanandroid.bean.ResponseBean
import com.pp.ui.utils.StateView

fun <Data> StateView.showResponse(response: ResponseBean<List<Data>>) {

    if (response.errorCode != WanAndroidService.ErrorCode.SUCCESS) {
        showError(Throwable(response.errorMsg))
        return
    }
    if (response.data?.isNotEmpty() == true) {
        showContent()
    } else {
        showEmpty()
    }
}
package com.pp.home.repository

import com.pp.common.http.wanandroid.api.WanAndroidService
import com.pp.common.http.wanandroid.bean.ResponseBean
import com.pp.common.http.wanandroid.bean.home.BannerBean

object HomeRepository {
    suspend fun getBanner(): ResponseBean<List<BannerBean>> {
        return WanAndroidService.homeApi.getBanner()
    }
}
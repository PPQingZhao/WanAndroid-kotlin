package com.pp.common.http.wanandroid.api

import com.pp.common.http.wanandroid.bean.ResponseBean
import com.pp.common.http.wanandroid.bean.home.BannerBean
import retrofit2.http.GET

interface HomeApi {

    /**
     * 获取首页轮播图
     * https://www.wanandroid.com/banner/json
     */
    @GET("banner/json")
    suspend fun getBanner(): ResponseBean<List<BannerBean>>
}
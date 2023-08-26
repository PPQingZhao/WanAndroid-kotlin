package com.pp.common.http.wanandroid.api

import com.pp.common.http.wanandroid.bean.ArticleCidBean
import com.pp.common.http.wanandroid.bean.ResponseBean
import retrofit2.http.GET

interface NavigationApi {
    /**
     * https://www.wanandroid.com/navi/json
     * 项目为包含一个分类，该接口返回整个分类。
     */
    @GET("/navi/json")
    suspend fun getNavigation(): ResponseBean<List<ArticleCidBean>>
}
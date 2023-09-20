package com.pp.common.http.wanandroid.api

import com.pp.common.http.wanandroid.bean.ArticleListBean
import com.pp.common.http.wanandroid.bean.ArticlePageBean
import com.pp.common.http.wanandroid.bean.ResponseBean
import retrofit2.http.GET
import retrofit2.http.Path

interface WXArticleApi {
    /**
     * https://wanandroid.com/wxarticle/chapters/json
     * 公众号列表
     */
    @GET("/wxarticle/chapters/json")
    suspend fun getWXArticleList(): ResponseBean<List<ArticleListBean>>

    /**
     * https://wanandroid.com/wxarticle/list/408/1/json
     * 获取公众号历史文章
     * page: 从1开始
     */
    @GET("/wxarticle/list/{id}/{page}/json")
    suspend fun getWXArticle(
        @Path("id") id: Int,
        @Path("page") @androidx.annotation.IntRange(from = 1) page: Int,
    ): ResponseBean<ArticlePageBean>
}
package com.pp.common.http.wanandroid.api

import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.ResponseBean
import com.pp.common.http.wanandroid.bean.home.BannerBean
import retrofit2.http.GET
import retrofit2.http.Path

interface HomeApi {

    /**
     * 获取首页轮播图
     * https://www.wanandroid.com/banner/json
     */
    @GET("banner/json")
    suspend fun getBanner(): ResponseBean<List<BannerBean>>

    /**
     * 首页置顶文章列表
     * https://www.wanandroid.com/article/top/json
     */
    @GET("article/top/json")
    suspend fun getTopArticles(): ResponseBean<List<ArticleBean>>

    /**
     * 首页文章列表
     * https://www.wanandroid.com/article/list/0/json
     * 分页加载
     */
    @GET("article/list/{page}}/json")
    suspend fun getArticles(@Path("page") page: Int = 0): ResponseBean<List<ArticleBean>>
}
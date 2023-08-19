package com.pp.common.http.wanandroid.api

import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.PageBean
import com.pp.common.http.wanandroid.bean.ResponseBean
import com.pp.common.http.wanandroid.bean.home.BannerBean
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

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
     * 首页文章列表分页加载
     * https://www.wanandroid.com/article/list/0/json
     *
     * page:从0开始
     */
    @GET("article/list/{page}/json")
    suspend fun getArticles(@Path("page") @androidx.annotation.IntRange(from = 0) page: Int = 0): ResponseBean<PageBean>

    /**
     * 广场文章列表分页加载
     * https://www.wanandroid.com/user_article/list/0/json
     * page: 从0开始
     */
    @GET("user_article/list/{page}/json")
    suspend fun getSquareArticles(@Path("page") @androidx.annotation.IntRange(from = 0) page: Int = 0): ResponseBean<PageBean>

    /**
     * 问答文章列表分页加载
     * https://www.wanandroid.com/wenda/list/1/json
     *
     * page:从1开始
     */
    @GET("wenda/list/{page}/json")
    suspend fun getAnswerArticles(@Path("page") @androidx.annotation.IntRange(from = 1) page: Int = 1): ResponseBean<PageBean>
}
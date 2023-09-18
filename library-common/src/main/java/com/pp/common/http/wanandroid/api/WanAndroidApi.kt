package com.pp.common.http.wanandroid.api

import com.pp.common.http.wanandroid.bean.PageBean
import com.pp.common.http.wanandroid.bean.ResponseBean
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface WanAndroidApi {

    /**
     *
     * https://www.wanandroid.com/lg/collect/list/0/json
     * 收藏列表
     *
     * @param page 页码：拼接在链接中，从0开始。
     */
    @GET("lg/collect/list/{page}/json")
    suspend fun getCollectedArticles(@Path(value = "page") @androidx.annotation.IntRange(from = 0) page: Int = 0): ResponseBean<PageBean>

    /**
     * https://www.wanandroid.com/lg/collect/27241/json
     * 收藏站内文章
     * @param id 文章id
     */
    @POST("lg/collect/{id}/json")
    suspend fun collectArticle(@Path(value = "id") id: Int): ResponseBean<Any>

    /**
     * https://www.wanandroid.com/lg/uncollect_originId/27241/json
     * 取消收藏
     */
    @POST("lg/uncollect_originId/{id}/json")
    suspend fun unCollectedArticle(@Path(value = "id") id: Int): ResponseBean<Any>
}
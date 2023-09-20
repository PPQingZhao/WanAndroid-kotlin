package com.pp.common.http.wanandroid.api

import com.pp.common.http.wanandroid.bean.ArticleListBean
import com.pp.common.http.wanandroid.bean.ArticlePageBean
import com.pp.common.http.wanandroid.bean.ResponseBean
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SystemApi {
    /**
     * https://www.wanandroid.com/tree/json
     * 体系数据
     */
    @GET("/tree/json")
    suspend fun getSystem(): ResponseBean<List<ArticleListBean>>

    /**
     * https://www.wanandroid.com/article/list/0/json?cid=60
     * 体系文章
     * page : 从0 开始
     */
    @GET("/article/list/{page}/json")
    suspend fun getSystemArticle(
        @Path("page") @androidx.annotation.IntRange(from = 0) page: Int,
        @Query("cid") cid: Int,
    ): ResponseBean<ArticlePageBean>

}
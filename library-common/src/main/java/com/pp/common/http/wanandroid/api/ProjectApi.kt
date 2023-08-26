package com.pp.common.http.wanandroid.api

import com.pp.common.http.wanandroid.bean.ArticleListBean
import com.pp.common.http.wanandroid.bean.PageBean
import com.pp.common.http.wanandroid.bean.ResponseBean
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProjectApi {


    /**
     * https://www.wanandroid.com/project/tree/json
     *
     * 该接口返回整个项目分类
     */
    @GET("/project/tree/json")
    suspend fun getProjectCid(): ResponseBean<List<ArticleListBean>>

    /**
     * https://www.wanandroid.com/project/list/1/json?cid=294
     * 获取某一个分类下项目列表数据，分页展示
     * @param cid 项目分类的id
     * @param page 页码,从1开始
     */
    @GET("/project/list/{page}/json")
    suspend fun getProject(
        @Path("page") @androidx.annotation.IntRange(from = 1) page: Int = 1,
        @Query("cid") cid: Int,
    ): ResponseBean<PageBean>
}
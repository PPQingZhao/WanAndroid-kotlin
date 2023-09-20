package com.pp.common.http.wanandroid.api

import com.pp.common.http.wanandroid.bean.HotKeyBean
import com.pp.common.http.wanandroid.bean.ArticlePageBean
import com.pp.common.http.wanandroid.bean.ResponseBean
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SearchApi {

    /**
     * https://www.wanandroid.com/article/query/0/json?k=动画 Studio3
     * 该接口支持传入 page_size 控制分页数量，取值为[1-40]，不传则使用默认值，一旦传入了 page_size，后续该接口分页都需要带上，否则会造成分页读取错误。
     * 支持多个关键词，用空格隔开
     */
    @FormUrlEncoded
    @POST("article/query/{page}/json")
    suspend fun queryArticle(
        @Path("page") @androidx.annotation.IntRange(from = 0, to = 40) page: Int = 0,
        @Field("k") key: String?,
    ): ResponseBean<ArticlePageBean>

    /**
     * https://www.wanandroid.com/hotkey/json
     * 搜索热词
     */
    @GET("hotkey/json")
    suspend fun getHotkey(): ResponseBean<List<HotKeyBean>>
}
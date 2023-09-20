package com.pp.common.http.wanandroid.api

import com.pp.common.http.wanandroid.bean.*
import retrofit2.http.GET
import retrofit2.http.Path

interface CoinApi {

    /**
     * https://www.wanandroid.com/lg/coin/userinfo/json
     * 获取个人积分
     */
    @GET("lg/coin/userinfo/json")
    suspend fun getCoinInfo(): ResponseBean<CoinInfoBean>

    /**
     * https://www.wanandroid.com//lg/coin/list/1/json
     * 个人积分得分历史
     */
    @GET("lg/coin/list/{page}/json")
    suspend fun getCoinList(@Path(value = "page") @androidx.annotation.IntRange(from = 1) page: Int): ResponseBean<PageBean<CoinReasonBean>>

    /**
     * https://www.wanandroid.com/coin/rank/1/json
     * 积分排行榜
     */
    @GET("coin/rank/{page}/json")
    suspend fun getCoinRank(@Path(value = "page") @androidx.annotation.IntRange(from = 1) page: Int): ResponseBean<PageBean<CoinInfoBean>>
}
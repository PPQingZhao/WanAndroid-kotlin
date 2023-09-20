package com.pp.common.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pp.common.http.wanandroid.api.WanAndroidService
import com.pp.common.http.wanandroid.bean.*
import com.pp.common.paging.WanPagingSource
import kotlinx.coroutines.flow.Flow

object CoinRepository {

    /**
     * 获取个人积分
     */
    suspend fun getCoinInfo(): ResponseBean<CoinInfoBean> {
        return WanAndroidService.coinApi.getCoinInfo()
    }

    /**
     * 获取个人得分历史
     */
    fun getCoinList(): Flow<PagingData<CoinReasonBean>> {
        return Pager(
            initialKey = 1,
            config = PagingConfig(15),
            pagingSourceFactory = { CoinListPageSources() }).flow
    }

    /**
     * 积分排行榜
     */
    fun getCoinRange(): Flow<PagingData<CoinInfoBean>> {
        return Pager(
            initialKey = 1,
            config = PagingConfig(15),
            pagingSourceFactory = { CoinRangePageSources() }).flow
    }

    private class CoinListPageSources() : WanPagingSource<CoinReasonBean>() {
        override suspend fun getPageData(page: Int): PageBean<CoinReasonBean>? {
            return WanAndroidService.coinApi.getCoinList(page).data
        }

        override fun createNextKey(response: PageBean<CoinReasonBean>?): Int? {
            return super.createNextKey(response)?.plus(1)
        }
    }

    private class CoinRangePageSources() : WanPagingSource<CoinInfoBean>() {
        override suspend fun getPageData(page: Int): PageBean<CoinInfoBean>? {
            return WanAndroidService.coinApi.getCoinRank(page).data
        }

        override fun createNextKey(response: PageBean<CoinInfoBean>?): Int? {
            return super.createNextKey(response)?.plus(1)
        }
    }
}
package com.pp.home.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pp.common.http.wanandroid.api.WanAndroidService
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.PageBean
import com.pp.common.paging.WanPagingSource
import kotlinx.coroutines.flow.Flow

object SquareRepository {
    private const val startPage = 0
    fun getPageData(): Flow<PagingData<ArticleBean>> {
        return Pager(
            initialKey = startPage,
            config = PagingConfig(15),
            pagingSourceFactory = { SquarePagingSource() }).flow
    }


    private class SquarePagingSource : WanPagingSource() {

        override suspend fun getPageData(page: Int): PageBean? {
            return WanAndroidService.homeApi.getSquareArticles(page).data
        }


    }

}
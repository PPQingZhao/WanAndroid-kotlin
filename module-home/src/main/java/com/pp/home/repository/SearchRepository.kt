package com.pp.home.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pp.common.http.wanandroid.api.WanAndroidService
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.HotKey
import com.pp.common.http.wanandroid.bean.PageBean
import com.pp.common.http.wanandroid.bean.ResponseBean
import com.pp.common.paging.DefaultSimplePagingSource
import com.pp.common.paging.WanPagingSource
import kotlinx.coroutines.flow.Flow

object SearchRepository {

    suspend fun getHotKey(): ResponseBean<List<HotKey>> {
        return WanAndroidService.searchApi.getHotkey()
    }

    fun searchPageData(key: String?): Flow<PagingData<ArticleBean>> {
        return Pager(
            initialKey = 0,
            config = PagingConfig(15),
            pagingSourceFactory = { SearchPageSources(key) }).flow
    }

    private class SearchPageSources(private val key: String?) : WanPagingSource() {

        override suspend fun getPageData(page: Int): PageBean? {
            return WanAndroidService.searchApi.queryArticle(page, key).data
        }

        override fun createNextKey(response: PageBean?): Int? {
            return response?.curPage?.plus(1)
        }

    }
}
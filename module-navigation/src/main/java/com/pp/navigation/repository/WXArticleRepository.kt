package com.pp.navigation.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pp.common.http.wanandroid.api.WanAndroidService
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.ArticleListBean
import com.pp.common.http.wanandroid.bean.PageBean
import com.pp.common.http.wanandroid.bean.ResponseBean
import com.pp.common.paging.WanPagingSource
import kotlinx.coroutines.flow.Flow

object WXArticleRepository {


    suspend fun getWXArticleList(): ResponseBean<List<ArticleListBean>> {
        return WanAndroidService.wxArticleApi.getWXArticleList()
    }

    fun getWXArticle(id: Int): Flow<PagingData<ArticleBean>> {
        return Pager(config = PagingConfig(15), initialKey = 1, pagingSourceFactory = {
            WXArticlePagingSource(id)
        }).flow
    }

    private class WXArticlePagingSource(val id: Int) : WanPagingSource() {

        override suspend fun getPageData(page: Int): PageBean? {
            return WanAndroidService.wxArticleApi.getWXArticle(id, page).data
        }

        override fun createNextKey(response: PageBean?): Int? {
            return response?.curPage?.plus(1)
        }

    }

}
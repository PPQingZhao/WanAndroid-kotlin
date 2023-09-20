package com.pp.navigation.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pp.common.http.wanandroid.api.WanAndroidService
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.ArticleListBean
import com.pp.common.http.wanandroid.bean.ArticlePageBean
import com.pp.common.http.wanandroid.bean.ResponseBean
import com.pp.common.paging.ArticlePagingSource
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

    private class WXArticlePagingSource(val id: Int) : ArticlePagingSource() {

        override suspend fun getPageData(page: Int): ArticlePageBean? {
            return WanAndroidService.wxArticleApi.getWXArticle(id, page).data
        }

        override fun createNextKey(response: ArticlePageBean?): Int? {
            return super.createNextKey(response)?.plus(1)
        }

    }

}
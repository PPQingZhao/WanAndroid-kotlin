package com.pp.home.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pp.common.http.wanandroid.api.WanAndroidService
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.ArticlePageBean
import com.pp.common.paging.ArticlePagingSource
import kotlinx.coroutines.flow.Flow

object AnswerRepository {
    private const val startPage = 1
    fun getPageData(): Flow<PagingData<ArticleBean>> {
        return Pager(
            initialKey = startPage,
            config = PagingConfig(15),
            pagingSourceFactory = { AnswerPageSources() }).flow
    }

    private class AnswerPageSources : ArticlePagingSource() {

        override suspend fun getPageData(page: Int): ArticlePageBean? {
            return WanAndroidService.homeApi.getAnswerArticles(page).data
        }

        override fun createNextKey(response: ArticlePageBean?): Int? {
            return super.createNextKey(response)?.plus(1)
        }

    }

}
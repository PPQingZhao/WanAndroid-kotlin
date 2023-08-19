package com.pp.home.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pp.common.http.wanandroid.api.WanAndroidService
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.PageBean
import com.pp.common.http.wanandroid.bean.ResponseBean
import com.pp.common.paging.ArticlePagingSource
import kotlinx.coroutines.flow.Flow

object AnswerRepository {
    private const val startPage = 1
    fun getPageData(): Flow<PagingData<ArticleBean>> {
        return Pager(
            initialKey = startPage,
            config = PagingConfig(15),
            pagingSourceFactory = { SquareArticlePagingSource() }).flow
    }

    private class SquareArticlePagingSource : ArticlePagingSource() {
        override suspend fun getArticlesApi(page: Int): ResponseBean<PageBean> {
            return WanAndroidService.homeApi.getAnswerArticles(page)
        }

        override fun createNextKey(it: PageBean): Int? {
            return super.createNextKey(it)?.plus(1)
        }
    }

}
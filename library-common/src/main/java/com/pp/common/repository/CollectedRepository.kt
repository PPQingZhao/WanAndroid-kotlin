package com.pp.common.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pp.common.http.wanandroid.api.WanAndroidService
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.PageBean
import com.pp.common.http.wanandroid.bean.ResponseBean
import com.pp.common.paging.WanPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object CollectedRepository {

    private val _collected = MutableSharedFlow<ArticleBean>()

    val collected = _collected.asSharedFlow()

    /**
     * 获取收藏列表
     */
    fun getCollectedPageData(): Flow<PagingData<ArticleBean>> {
        return Pager(
            initialKey = 0,
            config = PagingConfig(15),
            pagingSourceFactory = { CollectedPageSources() }).flow
    }

    /**
     * 收藏
     */
    suspend fun collected(articleBean: ArticleBean): ResponseBean<Any> {
        return WanAndroidService.wanApi.collectArticle(articleBean.id).apply {
            if (errorCode == WanAndroidService.ErrorCode.SUCCESS) {
                articleBean.collect = true
                _collected.emit(articleBean)
            }
        }
    }

    /**
     * 收藏列表使用该方法取消收藏
     */
    suspend fun unCollected2(articleBean: ArticleBean): ResponseBean<Any> {
        return WanAndroidService.wanApi.unCollectedArticle(
            articleBean.id, articleBean.originId ?: -1
        ).apply {
            if (errorCode == WanAndroidService.ErrorCode.SUCCESS) {
                articleBean.collect = false
                _collected.emit(articleBean)
            }
        }
    }

    /**
     * 文章列表使用该方法取消收藏
     */
    suspend fun unCollected(articleBean: ArticleBean): ResponseBean<Any> {
        return WanAndroidService.wanApi.unCollectedArticle(articleBean.id).apply {
            if (errorCode == WanAndroidService.ErrorCode.SUCCESS) {
                articleBean.collect = false
                _collected.emit(articleBean)
            }
        }
    }

    private class CollectedPageSources() : WanPagingSource() {

        override suspend fun getPageData(page: Int): PageBean? {
            return WanAndroidService.wanApi.getCollectedArticles(page).data
        }

        override fun createNextKey(response: PageBean?): Int? {
            return super.createNextKey(response)?.plus(1)
        }

    }
}
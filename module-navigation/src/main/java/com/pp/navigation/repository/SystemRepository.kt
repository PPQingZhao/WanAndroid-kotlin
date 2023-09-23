package com.pp.navigation.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pp.common.http.wanandroid.api.WanAndroidService
import com.pp.common.http.wanandroid.bean.*
import com.pp.common.paging.ArticlePagingSource
import kotlinx.coroutines.flow.Flow

object SystemRepository {


    suspend fun getSystemList(): ResponseBean<List<ArticleListBean>> {
        return runCatchingResponse {
            WanAndroidService.systemApi.getSystem()
        }
    }

    fun getSystemArticle(cid: Int): Flow<PagingData<ArticleBean>> {
        return Pager(config = PagingConfig(15), initialKey = 0, pagingSourceFactory = {
            SystemArticlePagingSource(cid)
        }).flow
    }

    private class SystemArticlePagingSource(val cid: Int) : ArticlePagingSource() {

        override suspend fun getPageData(page: Int): ArticlePageBean? {
            return WanAndroidService.systemApi.getSystemArticle(cid = cid, page = page).data
        }

    }

}
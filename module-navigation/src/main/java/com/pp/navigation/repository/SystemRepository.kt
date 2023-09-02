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

object SystemRepository {


    suspend fun getSystemList(): ResponseBean<List<ArticleListBean>> {
        return WanAndroidService.systemApi.getSystem()
    }

    fun getSystemArticle(cid: Int): Flow<PagingData<ArticleBean>> {
        return Pager(config = PagingConfig(15), initialKey = 1, pagingSourceFactory = {
            SystemArticlePagingSource(cid)
        }).flow
    }

    private class SystemArticlePagingSource(val cid: Int) : WanPagingSource() {

        override suspend fun getPageData(page: Int): PageBean? {
            return WanAndroidService.systemApi.getSystemArticle(cid, page).data
        }

    }

}
package com.pp.project.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pp.common.http.wanandroid.api.WanAndroidService
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.ArticleCidBean
import com.pp.common.http.wanandroid.bean.PageBean
import com.pp.common.paging.WanPagingSource
import kotlinx.coroutines.flow.Flow

object ProjectRepository {

    suspend fun getProjectCid(): List<ArticleCidBean> {
        return WanAndroidService.projectApi.getProjectCid().data ?: emptyList()
    }

    fun getPageData(cid: Int): Flow<PagingData<ArticleBean>> {
        return Pager(
            initialKey = 1,
            config = PagingConfig(15),
            pagingSourceFactory = { ProjectPageSources(cid) }).flow
    }

    private class ProjectPageSources(val cid: Int) : WanPagingSource() {

        override suspend fun getPageData(page: Int): PageBean? {
            return WanAndroidService.projectApi.getProject(page,cid).data
        }

        override fun createNextKey(response: PageBean?): Int? {
            return response?.curPage?.plus(1)
        }

    }
}
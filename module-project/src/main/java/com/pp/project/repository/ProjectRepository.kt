package com.pp.project.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pp.common.http.wanandroid.api.WanAndroidService
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.ArticleListBean
import com.pp.common.http.wanandroid.bean.ArticlePageBean
import com.pp.common.paging.ArticlePagingSource
import kotlinx.coroutines.flow.Flow

object ProjectRepository {

    suspend fun getProjectCid(): List<ArticleListBean> {
        return WanAndroidService.projectApi.getProjectCid().data ?: emptyList()
    }

    fun getPageData(cid: Int): Flow<PagingData<ArticleBean>> {
        return Pager(
            initialKey = 1,
            config = PagingConfig(15),
            pagingSourceFactory = { ProjectPageSources(cid) }).flow
    }

    private class ProjectPageSources(val cid: Int) : ArticlePagingSource() {

        override suspend fun getPageData(page: Int): ArticlePageBean? {
            return WanAndroidService.projectApi.getProject(page,cid).data
        }

        override fun createNextKey(response: ArticlePageBean?): Int? {
            return super.createNextKey(response)?.plus(1)
        }

    }
}
package com.pp.home.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pp.common.http.wanandroid.api.WanAndroidService
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.PageBean
import com.pp.common.http.wanandroid.bean.ResponseBean
import com.pp.common.http.wanandroid.bean.home.BannerBean
import com.pp.common.paging.ArticlePagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

object HomeRepository {
    private const val startPage = 0
    suspend fun getBanner(): ResponseBean<List<BannerBean>> {
        return WanAndroidService.homeApi.getBanner()
    }

    fun getPageData(): Flow<PagingData<ArticleBean>> {
        return Pager(
            initialKey = startPage,
            config = PagingConfig(15),
            pagingSourceFactory = { HomeArticlePagingSource() }).flow
    }

    private class HomeArticlePagingSource : ArticlePagingSource() {
        override suspend fun getArticlesApi(page: Int): ResponseBean<PageBean> {
            return WanAndroidService.homeApi.getArticles(page)
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleBean> {
            return try {
                val page = params.key ?: return LoadResult.Page(emptyList(), null, null)
                val dataList = mutableListOf<ArticleBean>()

                if (page == 0) {
                    withContext(Dispatchers.IO) {
                        //  获取置顶文章
                        val topList = async {
                            WanAndroidService.homeApi.getTopArticles().data
                        }

                        // 获取文章列表
                        val loadResultDeferred = async {
                            super.load(params)
                        }

                        topList.await()?.let {
                            dataList.addAll(it)
                        }

                        loadResultDeferred.await().let {
                            if (it is LoadResult.Page<Int, ArticleBean>) {
                                dataList.addAll(it.data)
                                LoadResult.Page(dataList, it.prevKey, it.nextKey)
                            } else {
                                it
                            }
                        }
                    }
                } else {
                    super.load(params)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                LoadResult.Error(e)
            }

        }

    }

}
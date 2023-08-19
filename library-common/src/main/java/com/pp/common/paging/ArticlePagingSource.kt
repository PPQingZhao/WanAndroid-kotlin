package com.pp.common.paging

import androidx.paging.PagingSource
import com.pp.common.http.wanandroid.api.WanAndroidService
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.PageBean
import com.pp.common.http.wanandroid.bean.ResponseBean

abstract class ArticlePagingSource : PagingSource<Int, ArticleBean>() {
    abstract suspend fun getArticlesApi(page: Int): ResponseBean<PageBean>
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleBean> {
        return try {
            val key = params.key ?: return LoadResult.Page(emptyList(), null, null)
            val dataList = mutableListOf<ArticleBean>()
            var nextKey: Int? = null

            getArticlesApi(key).data?.let {
                nextKey = createNextKey(it)
                dataList.addAll(it.datas)
            }

            LoadResult.Page(dataList, null, nextKey)
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    open fun createNextKey(it: PageBean): Int? {
        return it.curPage
    }

}
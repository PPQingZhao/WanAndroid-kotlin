package com.pp.common.paging

import androidx.paging.PagingSource
import com.pp.common.http.wanandroid.api.WanAndroidService
import com.pp.common.http.wanandroid.bean.ArticleBean

open class ArticlePagingSource : PagingSource<Int, ArticleBean>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleBean> {
        return try {
            val key = params.key ?: return LoadResult.Page(emptyList(), null, null)
            val dataList = mutableListOf<ArticleBean>()
            var nextKey: Int? = null
            val page = key

            WanAndroidService.homeApi.getArticles(page).data?.let {
                nextKey = it.curPage
                dataList.addAll(it.datas)
            }


            LoadResult.Page(dataList, null, nextKey)
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

}
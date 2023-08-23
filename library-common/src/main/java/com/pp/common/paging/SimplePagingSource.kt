package com.pp.common.paging

import androidx.paging.PagingSource

abstract class SimplePagingSource<PageData : Any, Key : Any, Value : Any> :
    PagingSource<Key, Value>() {

    abstract suspend fun getPageData(page: Key): PageData?

    override suspend fun load(params: LoadParams<Key>): LoadResult<Key, Value> {
        return try {
            val key = params.key ?: return LoadResult.Page(emptyList(), null, null)
            val dataList = mutableListOf<Value>()
            val nextKey: Key?

            val pageData = getPageData(key)

            nextKey = createNextKey(pageData)
            val list = getPageValue(pageData)
            dataList.addAll(list)

            LoadResult.Page(dataList, null, nextKey)
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    abstract fun getPageValue(pageData: PageData?): List<Value>

    abstract fun createNextKey(response: PageData?): Key?

}
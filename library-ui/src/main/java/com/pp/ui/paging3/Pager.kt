package com.pp.common.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig

fun <PageData : Any, Value : Any> onePager(
    getPageData: suspend () -> PageData?,
    getPageValue: (pageData: PageData?) -> List<Value>,
): Pager<Int, Value> {
    return Pager(
        initialKey = 0,
        config = PagingConfig(15),
        pagingSourceFactory = {
            DefaultSimplePagingSource<PageData, Int, Value>(
                getPageData = { getPageData.invoke() },
                getPageValue = getPageValue,
                createNextKey = { null })
        })
}

fun <PageData : Any, Value : Any> emptyPager(): Pager<Int, Value> {
    return onePager(getPageValue = { emptyList<Value>() }, getPageData = { emptyList<PageData>() })
}
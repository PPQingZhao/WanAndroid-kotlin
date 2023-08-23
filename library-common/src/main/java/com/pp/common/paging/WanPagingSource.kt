package com.pp.common.paging

import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.PageBean

abstract class WanPagingSource : SimplePagingSource<PageBean, Int, ArticleBean>() {

    override fun getPageValue(pageData: PageBean?): List<ArticleBean> {
        return pageData?.datas ?: emptyList()
    }

    override fun createNextKey(response: PageBean?): Int? {
        return response?.curPage
    }
}
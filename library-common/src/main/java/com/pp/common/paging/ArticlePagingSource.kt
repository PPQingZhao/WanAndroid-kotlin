package com.pp.common.paging

import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.ArticlePageBean
import com.pp.common.http.wanandroid.bean.PageBean

abstract class ArticlePagingSource : SimplePagingSource<ArticlePageBean, Int, ArticleBean>() {

    override fun getPageValue(pageData: ArticlePageBean?): List<ArticleBean> {
        return pageData?.datas ?: emptyList()
    }

    override fun createNextKey(response: ArticlePageBean?): Int? {
        return response?.run {
            if (over) {
                null
            } else {
                curPage
            }
        }
    }
}

abstract class WanPagingSource<Data : Any> : SimplePagingSource<PageBean<Data>, Int, Data>() {

    override fun getPageValue(pageData: PageBean<Data>?): List<Data> {
        return pageData?.datas ?: emptyList()
    }

    override fun createNextKey(response: PageBean<Data>?): Int? {
        return response?.run {
            if (over) {
                null
            } else {
                curPage
            }
        }
    }
}
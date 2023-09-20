package com.pp.common.http.wanandroid.bean

open class PageBean<Data>(
    val curPage: Int = 0,
    val datas: List<Data> = listOf(),
    val offset: Int = 0,
    val over: Boolean = false,
    val pageCount: Int = 0,
    val size: Int = 0,
    val total: Int = 0,
)
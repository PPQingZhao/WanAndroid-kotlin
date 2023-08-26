package com.pp.common.http.wanandroid.bean

data class ArticleListBean(
//    val articleList: List<ArticleBean> = listOf(),
    val articles: List<ArticleBean> = listOf(),
    val author: String = "",
    val children: List<Any> = listOf(),
    val courseId: Int = 0,
    val cover: String = "",
    val desc: String = "",
    val id: Int = 0,
    val lisense: String = "",
    val lisenseLink: String = "",
    val name: String = "",
    val order: Int = 0,
    val parentChapterId: Int = 0,
    val type: Int = 0,
    val userControlSetTop: Boolean = false,
    val visible: Int = 0,
    val cid:Long = 0
)
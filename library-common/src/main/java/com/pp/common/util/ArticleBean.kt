package com.pp.common.util

import com.pp.common.http.wanandroid.bean.ArticleBean

fun ArticleBean.getAuthor(): String {
    return author.ifEmpty { shareUser }
}

fun ArticleBean.getCharterName(): String {
    return superChapterName + if (chapterName.isNotEmpty()) "/${chapterName}" else ""
}
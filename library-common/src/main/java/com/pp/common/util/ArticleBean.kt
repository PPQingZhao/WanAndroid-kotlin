package com.pp.common.util

import com.pp.common.http.wanandroid.bean.ArticleBean

fun ArticleBean.getAuthor(): String {
    return author.ifEmpty { shareUser }
}

fun ArticleBean.getCharterName(): String {
    return superChapterName + if (chapterName.isNotEmpty()) "/${chapterName}" else ""
}

fun ArticleBean.getTags(): String {
    val tagsBuilder = StringBuilder()
    tags.onEachIndexed { index, tag ->
        tagsBuilder.append(tag.name).append(if (index < tags.size - 1) "·" else "")
    }
    return tagsBuilder.toString().trim()
}
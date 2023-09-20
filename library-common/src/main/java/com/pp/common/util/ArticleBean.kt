package com.pp.common.util

import com.pp.ui.R
import com.pp.common.app.App
import com.pp.common.http.wanandroid.bean.ArticleBean

fun ArticleBean.getAuthor(): String? {
    return author?.ifEmpty {
        shareUser?.ifEmpty {
            App.getInstance().getString(R.string.anonymity)
        }
    }
}

fun ArticleBean.getCharterName(): String {
    return superChapterName + if (chapterName?.isNotEmpty() == true) "/${chapterName}" else ""
}

fun ArticleBean.getTags(): String {
    val tagsBuilder = StringBuilder()
    tags.onEachIndexed { index, tag ->
        tagsBuilder.append(tag.name).append(if (index < tags.size - 1) "·" else "")
    }
    return tagsBuilder.toString().trim()
}
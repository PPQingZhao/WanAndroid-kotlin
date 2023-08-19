package com.pp.home.model

import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.util.getAuthor
import com.pp.common.util.getCharterName
import com.pp.theme.AppDynamicTheme
import com.pp.ui.viewModel.ItemArticleViewModel

class HomeItemArticleViewModel(articleBean: ArticleBean?, theme: AppDynamicTheme) :
    ItemArticleViewModel(theme) {
    var article: ArticleBean? = null
        set(value) {
            if (field == value) {
                return
            }

            field = value

            author.set(field?.getAuthor())
            niceDate.set(field?.niceDate)
            title.set(field?.title)
            chapterName.set(field?.getCharterName())

            isFresh.set(field?.fresh == true)
            isPinned.set(field?.type == 1)
            isCollect.set(field?.collect == true)
        }

    init {
        article = articleBean
    }
}
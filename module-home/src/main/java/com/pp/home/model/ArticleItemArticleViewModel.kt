package com.pp.home.model

import android.view.View
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.util.getAuthor
import com.pp.common.util.getCharterName
import com.pp.common.util.getTags
import com.pp.theme.AppDynamicTheme
import com.pp.ui.viewModel.ItemArticleViewModel

open class ArticleItemArticleViewModel(articleBean: ArticleBean?, theme: AppDynamicTheme) :
    ItemArticleViewModel(theme) {
    private var article: ArticleBean? = null
        set(value) {
            if (field == value) {
                return
            }

            field = value

            author.set(field?.getAuthor())
            niceDate.set(field?.niceDate)
            title.set(field?.title)
            chapterName.set(field?.getCharterName())

            tags.set(field?.getTags())

            isFresh.set(field?.fresh == true)
            isPinned.set(field?.type == 1)
            isCollect.set(field?.collect == true)
        }

    init {
        updateArticle(articleBean)
    }

    open fun updateArticle(articleBean: ArticleBean?) {
        this.article = articleBean
    }

    override fun onCollect(v: View) {
        isCollect.let {
            it.set(it.get().not())
            article?.collect = it.get()
        }
    }
}
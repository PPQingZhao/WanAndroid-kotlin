package com.pp.home.model

import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.theme.AppDynamicTheme

class ChapterItemArticleViewModel(articleBean: ArticleBean?, theme: AppDynamicTheme) :
    ArticleItemArticleViewModel(articleBean, theme) {

    override fun updateArticle(articleBean: ArticleBean?) {
        super.updateArticle(articleBean)
        chapterName.set(articleBean?.chapterName)
    }
}
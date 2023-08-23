package com.pp.common.model

import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.theme.AppDynamicTheme

class ChapterItemArticleViewModel(articleBean: ArticleBean?, theme: AppDynamicTheme) :
    ArticleItemArticleViewModel(articleBean, theme) {

    override fun onUpdateArticle(articleBean: ArticleBean?) {
        chapterName.set(articleBean?.chapterName)
    }
}
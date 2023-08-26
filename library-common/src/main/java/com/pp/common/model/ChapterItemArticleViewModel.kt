package com.pp.common.model

import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.theme.AppDynamicTheme

class ChapterItemArticleViewModel(articleBean: ArticleBean?, theme: AppDynamicTheme) :
    ArticleItemArticleViewModel(articleBean, theme) {

    override fun onUpdateData(data: ArticleBean?) {
        super.onUpdateData(data)
        chapterName.set(data?.chapterName)

    }
}
package com.pp.common.model

import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.theme.AppDynamicTheme
import kotlinx.coroutines.CoroutineScope

class ChapterItemArticleViewModel(
    articleBean: ArticleBean?,
    theme: AppDynamicTheme,
    scope: CoroutineScope
) :
    ArticleItemArticleViewModel(articleBean, theme, scope) {

    override fun onUpdateData(data: ArticleBean?) {
        super.onUpdateData(data)
        chapterName.set(data?.chapterName)

    }
}
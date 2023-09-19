package com.pp.common.model

import com.pp.common.http.wanandroid.bean.ArticleListBean
import com.pp.theme.AppDynamicTheme
import com.pp.ui.viewModel.ItemTextViewModel

class ItemArticleListTextViewModel(
    cidBean: ArticleListBean?,
    theme: AppDynamicTheme,
) :
    ItemTextViewModel<ArticleListBean>(theme) {

    init {
        data = cidBean
    }

    override fun onUpdateData(data: ArticleListBean?) {
        text.set(data?.name)
    }
}
package com.pp.common.model

import android.view.View
import com.pp.common.http.wanandroid.bean.ArticleListBean
import com.pp.common.model.ItemSelectedModel
import com.pp.theme.AppDynamicTheme
import com.pp.ui.viewModel.ItemTextViewModel

class ItemArticleListTextViewModel(
    private val selectedItem: ItemSelectedModel<ArticleListBean, ItemTextViewModel<ArticleListBean>>? = null,
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

    override fun onItemClick(view: View) {
        if (null == selectedItem) {
            super.onItemClick(view)
        } else {
            selectedItem.selectedItem(this)
        }
    }

}
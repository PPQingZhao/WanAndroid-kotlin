package com.pp.common.paging

import android.annotation.SuppressLint
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.model.ArticleItemArticleViewModel
import com.pp.common.model.ChapterItemArticleViewModel
import com.pp.theme.AppDynamicTheme
import com.pp.ui.adapter.BindingPagingDataAdapter
import com.pp.ui.databinding.ItemArticleBinding


val articleDifferCallback = object : DiffUtil.ItemCallback<ArticleBean>() {
    override fun areItemsTheSame(oldItem: ArticleBean, newItem: ArticleBean): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ArticleBean, newItem: ArticleBean): Boolean {
        return oldItem == newItem
    }
}


fun itemArticlePagingAdapter(layoutInflater: LayoutInflater, theme: AppDynamicTheme) =
    BindingPagingDataAdapter.DefaultBindingPagingDataAdapter<ItemArticleBinding, ArticleItemArticleViewModel, ArticleBean>(
        onCreateViewDataBinding = { ItemArticleBinding.inflate(layoutInflater, it, false) },
        onBindItemViewModel = { _, item, _, cachedItemModel ->
            if (cachedItemModel is ArticleItemArticleViewModel) {
                cachedItemModel.also { it.data = item }
            } else {
                ArticleItemArticleViewModel(item, theme)
            }
        },
        diffCallback = articleDifferCallback
    )

fun itemChapterArticlePagingAdapter(
    layoutInflater: LayoutInflater,
    theme: AppDynamicTheme,
    onBindItemViewModel: (bind: ItemArticleBinding, ChapterItemArticleViewModel, pos: Int) -> Unit = { _, _, _ -> },
) =
    BindingPagingDataAdapter.DefaultBindingPagingDataAdapter<ItemArticleBinding, ChapterItemArticleViewModel, ArticleBean>(
        onCreateViewDataBinding = { ItemArticleBinding.inflate(layoutInflater, it, false) },
        onBindItemViewModel = { bind, item, pos, cachedItemModel ->
            if (cachedItemModel is ChapterItemArticleViewModel) {
                cachedItemModel.also { it.data = item }
            } else {
                ChapterItemArticleViewModel(item, theme)
            }.apply {
                onBindItemViewModel.invoke(bind, this, pos)
            }
        },
        diffCallback = articleDifferCallback
    )
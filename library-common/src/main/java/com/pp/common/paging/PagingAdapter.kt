package com.pp.common.paging

import android.annotation.SuppressLint
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.ArticleListBean
import com.pp.common.model.*
import com.pp.theme.AppDynamicTheme
import com.pp.ui.adapter.BindingPagingDataAdapter
import com.pp.ui.adapter.DefaultViewDataBindingItemType
import com.pp.ui.adapter.RecyclerViewBindingAdapter
import com.pp.ui.databinding.*
import com.pp.ui.viewModel.ItemTextViewModel


val articleDifferCallback = object : DiffUtil.ItemCallback<ArticleBean>() {
    override fun areItemsTheSame(oldItem: ArticleBean, newItem: ArticleBean): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ArticleBean, newItem: ArticleBean): Boolean {
        return oldItem == newItem
    }
}

fun itemArticleListTextBindItemType(
    selectedItem: ItemSelectedModel<ArticleListBean, ItemTextViewModel<ArticleListBean>>,
    layoutInflater: LayoutInflater,
    theme: AppDynamicTheme,
) =
    DefaultViewDataBindingItemType<ItemTextBinding, ItemArticleListTextViewModel, ArticleListBean>(
        createBinding = { ItemTextBinding.inflate(layoutInflater, it, false) },
        onBindItemViewModel = { _, item, position, cachedItemModel ->
            if (cachedItemModel is ItemArticleListTextViewModel) {
                cachedItemModel.apply { data = item }
            } else {
                ItemArticleListTextViewModel(selectedItem, item, theme)
            }.apply {
                setPosition(position)
            }
        })

fun itemArticleListText1BindItemType(
    itemType: Int = 0,
    inflater: LayoutInflater,
    theme: AppDynamicTheme,
) = DefaultViewDataBindingItemType<ItemText1Binding, ItemArticleListTextViewModel, ArticleListBean>(
    createBinding = { ItemText1Binding.inflate(inflater, it, false) },
    onBindItemViewModel = { _, data, _, cacheViewModel ->
        cacheViewModel?.also {
            it.data = data
        } ?: ItemArticleListTextViewModel(null, data, theme)
    },
    getItemType = { itemType }
)

fun itemArticleText2BindItemType(
    itemType: Int = 0,
    inflater: LayoutInflater,
    theme: AppDynamicTheme,
) = DefaultViewDataBindingItemType<ItemText2Binding, ItemArticleTextViewModel, ArticleBean>(
    createBinding = {
        ItemText2Binding.inflate(inflater, it, false)
    },
    onBindItemViewModel = { _, data, position, cacheViewModel ->
        cacheViewModel?.also {
            it.data = data
        } ?: ItemArticleTextViewModel(data, theme)
    },
    getItemType = { itemType },
)

fun itemArticleText3BindItemType(
    itemType: Int = 0,
    inflater: LayoutInflater,
    theme: AppDynamicTheme,
) = DefaultViewDataBindingItemType<ItemText3Binding, ItemArticleListTextViewModel, ArticleListBean>(
    createBinding = {
        ItemText3Binding.inflate(inflater, it, false)
    },
    onBindItemViewModel = { _, data, position, cacheViewModel ->
        cacheViewModel?.also {
            it.data = data
        } ?: ItemArticleListTextViewModel(cidBean = data, theme = theme)
    },
    getItemType = { itemType },
)

fun itemArticleListText2BindItemType(
    itemType: Int = 0,
    inflater: LayoutInflater,
    theme: AppDynamicTheme,
) = DefaultViewDataBindingItemType<ItemText2Binding, ItemArticleListTextViewModel, ArticleListBean>(
    createBinding = {
        ItemText2Binding.inflate(inflater, it, false)
    },
    onBindItemViewModel = { _, data, position, cacheViewModel ->
        cacheViewModel?.also {
            it.data = data
        } ?: ItemArticleListTextViewModel(cidBean = data, theme = theme)
    },
    getItemType = { itemType },
)

val type_flexbox = 111
val recycledViewPool =
    RecyclerView.RecycledViewPool().apply { setMaxRecycledViews(type_flexbox, 100) }

fun itemArticleFlexBoxBindItemType(
    inflater: LayoutInflater,
    theme: AppDynamicTheme,
) =
    DefaultViewDataBindingItemType<ItemFlexboxTextBinding, ItemArticleListTextViewModel, ArticleListBean>(
        createBinding = {
            ItemFlexboxTextBinding.inflate(inflater, it, false).apply {
                recyclerview.setItemViewCacheSize(6)
                recyclerview.setRecycledViewPool(recycledViewPool)
                recyclerview.layoutManager = FlexboxLayoutManager(root.context)
                recyclerview.adapter =
                    RecyclerViewBindingAdapter.RecyclerViewBindingAdapterImpl(
                        itemArticleText2BindItemType(inflater = inflater, theme = theme),
                        getItemViewType = { type_flexbox }
                    )
            }
        },
        onBindItemViewModel = { bind, item, position, cachedItemModel ->
            (bind.recyclerview.adapter as RecyclerViewBindingAdapter.RecyclerViewBindingAdapterImpl<ItemText2Binding, ItemArticleTextViewModel, ArticleBean>)
                .setDataList(item?.articles ?: emptyList())

            cachedItemModel?.also {
                it.data = item
            } ?: ItemArticleListTextViewModel(cidBean = item, theme = theme)
        })


fun itemArticleListChildFlexBoxBindItemType(
    inflater: LayoutInflater,
    theme: AppDynamicTheme,
) =
    DefaultViewDataBindingItemType<ItemFlexboxTextBinding, ItemArticleListTextViewModel, ArticleListBean>(
        createBinding = {
            ItemFlexboxTextBinding.inflate(inflater, it, false).apply {
                recyclerview.setItemViewCacheSize(6)
                recyclerview.setRecycledViewPool(recycledViewPool)
                recyclerview.layoutManager = FlexboxLayoutManager(root.context)
                recyclerview.adapter =
                    RecyclerViewBindingAdapter.RecyclerViewBindingAdapterImpl(
                        itemArticleListText2BindItemType(inflater = inflater, theme = theme),
                        getItemViewType = { type_flexbox }
                    )
            }
        },
        onBindItemViewModel = { bind, item, position, cachedItemModel ->
            (bind.recyclerview.adapter as RecyclerViewBindingAdapter.RecyclerViewBindingAdapterImpl<ItemText2Binding, ItemArticleListTextViewModel, ArticleListBean>)
                .setDataList(item?.children ?: emptyList())

            cachedItemModel?.also {
                it.data = item
            } ?: ItemArticleListTextViewModel(cidBean = item, theme = theme)
        })


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
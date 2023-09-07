package com.pp.common.paging

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.ArticleListBean
import com.pp.common.http.wanandroid.bean.HotKey
import com.pp.common.model.*
import com.pp.theme.AppDynamicTheme
import com.pp.ui.R
import com.pp.ui.adapter.BindingPagingDataAdapter
import com.pp.ui.adapter.ItemDataViewModelBinder
import com.pp.ui.databinding.*
import com.pp.ui.viewModel.ItemDataViewModel
import com.pp.ui.viewModel.OnItemListener


val articleDifferCallback = object : DiffUtil.ItemCallback<ArticleBean>() {
    override fun areItemsTheSame(oldItem: ArticleBean, newItem: ArticleBean): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ArticleBean, newItem: ArticleBean): Boolean {
        return oldItem == newItem
    }
}

fun itemTextArticleListBinder(
    onItemListener: OnItemListener<ItemDataViewModel<ArticleListBean>>? = null,
    onBindViewModel: (binding: ItemTextBinding, data: ArticleListBean?, viewModel: ItemArticleListTextViewModel?, posiion: Int) -> Boolean = { _, _, _, _ -> false },
    theme: AppDynamicTheme,
) = ItemDataViewModelBinder<ItemTextBinding, ArticleListBean, ItemArticleListTextViewModel>(
    getItemViewModel = { ItemArticleListTextViewModel(it, theme) },
    getViewDataBindingClazz = { ItemTextBinding::class.java },
    getDataClazz = { ArticleListBean::class.java },
    onItemListener = onItemListener,
    onBindViewModel = onBindViewModel
)

fun itemText1ArticleListBinder(
    theme: AppDynamicTheme,
) = ItemDataViewModelBinder<ItemText1Binding, ArticleListBean, ItemArticleListTextViewModel>(
    getItemViewModel = { ItemArticleListTextViewModel(it, theme) },
    getViewDataBindingClazz = { ItemText1Binding::class.java },
    getDataClazz = { ArticleListBean::class.java },
)

fun itemText2ArticleBinder(
    theme: AppDynamicTheme,
) = ItemDataViewModelBinder<ItemText2Binding, ArticleBean, ItemArticleTextViewModel>(
    getItemViewModel = { ItemArticleTextViewModel(it, theme) },
    getViewDataBindingClazz = { ItemText2Binding::class.java },
    getDataClazz = { ArticleBean::class.java },
)

fun itemText3ArticleBinder(
    onItemListener: OnItemListener<ItemDataViewModel<ArticleListBean>>? = null,
    onBindViewModel: (binding: ItemText3Binding, data: ArticleListBean?, viewModel: ItemArticleListTextViewModel?, posiion: Int) -> Boolean = { _, _, _, _ -> false },
    theme: AppDynamicTheme,
) = ItemDataViewModelBinder<ItemText3Binding, ArticleListBean, ItemArticleListTextViewModel>(
    getItemViewModel = { ItemArticleListTextViewModel(it, theme) },
    getViewDataBindingClazz = { ItemText3Binding::class.java },
    getDataClazz = { ArticleListBean::class.java },
    onBindViewModel = onBindViewModel,
    onItemListener = onItemListener
)

fun itemText2ArticleListBinder(
    onItemListener: OnItemListener<ItemDataViewModel<ArticleListBean>>? = null,
    theme: AppDynamicTheme,
) = ItemDataViewModelBinder<ItemText2Binding, ArticleListBean, ItemArticleListTextViewModel>(
    getItemViewModel = { ItemArticleListTextViewModel(it, theme) },
    getViewDataBindingClazz = { ItemText2Binding::class.java },
    getDataClazz = { ArticleListBean::class.java },
    onItemListener = onItemListener
)

fun itemWXArticleBinder(
    theme: AppDynamicTheme,
) = ItemDataViewModelBinder<ItemWxArticleBinding, ArticleBean, ChapterItemArticleViewModel>(
    getItemViewModel = { ChapterItemArticleViewModel(it, theme) },
    getViewDataBindingClazz = { ItemWxArticleBinding::class.java },
    getDataClazz = { ArticleBean::class.java },
)

fun itemProjectArticleBinder(
    theme: AppDynamicTheme,
) = ItemDataViewModelBinder<ItemProjectarticleBinding, ArticleBean, ChapterItemArticleViewModel>(
    getItemViewModel = { ChapterItemArticleViewModel(it, theme) },
    getViewDataBindingClazz = { ItemProjectarticleBinding::class.java },
    getDataClazz = { ArticleBean::class.java },
)

fun itemArticlePagingAdapter(theme: AppDynamicTheme) =
    BindingPagingDataAdapter<ArticleBean>({
        R.layout.item_article
    }, diffCallback = articleDifferCallback).apply {
        itemArticleBinder(theme).also {
            addItemViewModelBinder(it)
        }
    }


fun itemText1HotkeyBinder(theme: AppDynamicTheme) =
    ItemDataViewModelBinder<ItemText1Binding, HotKey, ItemTextHotkeyViewModel>(
        getItemViewModel = { ItemTextHotkeyViewModel(it, theme) },
        getViewDataBindingClazz = { ItemText1Binding::class.java },
        getDataClazz = { HotKey::class.java }
    )

fun itemText2HotkeyBinder(
    onItemListener: OnItemListener<ItemDataViewModel<HotKey>>,
    theme: AppDynamicTheme,
) = ItemDataViewModelBinder<ItemText2Binding, HotKey, ItemTextHotkeyViewModel>(
    getItemViewModel = { ItemTextHotkeyViewModel(it, theme) },
    getViewDataBindingClazz = { ItemText2Binding::class.java },
    getDataClazz = { HotKey::class.java },
    onItemListener = onItemListener
)


fun itemArticleBinder(
    theme: AppDynamicTheme,
) = ItemDataViewModelBinder<ItemArticleBinding, ArticleBean, ArticleItemArticleViewModel>(
    getItemViewModel = { ArticleItemArticleViewModel(it, theme) },
    getViewDataBindingClazz = { ItemArticleBinding::class.java },
    getDataClazz = { ArticleBean::class.java },
)
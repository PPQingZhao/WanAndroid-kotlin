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
import com.pp.ui.adapter.ItemDataViewModelBinder.Companion.createItemBinder
import com.pp.ui.databinding.*
import com.pp.ui.viewModel.ItemDataViewModel
import com.pp.ui.viewModel.OnItemListener


val articleDifferCallback = object : DiffUtil.ItemCallback<ArticleBean>() {
    override fun areItemsTheSame(oldItem: ArticleBean, newItem: ArticleBean): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ArticleBean, newItem: ArticleBean): Boolean {
        return oldItem === newItem
    }
}

fun itemTextArticleListBinder(
    onItemListener: OnItemListener<ItemDataViewModel<ArticleListBean>>? = null,
    onBindViewModel: (binding: ItemTextBinding, data: ArticleListBean?, viewModel: ItemArticleListTextViewModel?, posiion: Int) -> Boolean = { _, _, _, _ -> false },
    theme: AppDynamicTheme,
) = createItemBinder<ItemTextBinding, ArticleListBean, ItemArticleListTextViewModel>(
    getItemViewModel = { data -> ItemArticleListTextViewModel(data, theme) },
    onItemListener = onItemListener,
    onBindViewModel = onBindViewModel
)

fun itemText1ArticleListBinder(
    theme: AppDynamicTheme,
) = createItemBinder<ItemText1Binding, ArticleListBean, ItemArticleListTextViewModel>(
    getItemViewModel = { data -> ItemArticleListTextViewModel(data, theme) },
)


fun itemText2ArticleBinder(
    theme: AppDynamicTheme,
) = createItemBinder<ItemText2Binding, ArticleBean, ItemArticleTextViewModel>(
    getItemViewModel = { data -> ItemArticleTextViewModel(data, theme) },
)

fun itemText3ArticleBinder(
    onItemListener: OnItemListener<ItemDataViewModel<ArticleListBean>>? = null,
    onBindViewModel: (binding: ItemText3Binding, data: ArticleListBean?, viewModel: ItemArticleListTextViewModel?, posiion: Int) -> Boolean = { _, _, _, _ -> false },
    theme: AppDynamicTheme,
) = createItemBinder<ItemText3Binding, ArticleListBean, ItemArticleListTextViewModel>(
    getItemViewModel = { data -> ItemArticleListTextViewModel(data, theme) },
    onItemListener = onItemListener,
    onBindViewModel = onBindViewModel
)

fun itemText2ArticleListBinder(
    onItemListener: OnItemListener<ItemDataViewModel<ArticleListBean>>? = null,
    theme: AppDynamicTheme,
) = createItemBinder<ItemText2Binding, ArticleListBean, ItemArticleListTextViewModel>(
    getItemViewModel = { data -> ItemArticleListTextViewModel(data, theme) },
    onItemListener = onItemListener
)

fun itemWXArticleBinder(
    theme: AppDynamicTheme,
) = createItemBinder<ItemWxArticleBinding, ArticleBean, ChapterItemArticleViewModel>(
    getItemViewModel = { data -> ChapterItemArticleViewModel(data, theme) },
)

fun itemProjectArticleBinder(
    theme: AppDynamicTheme,
) = createItemBinder<ItemProjectarticleBinding, ArticleBean, ChapterItemArticleViewModel>(
    getItemViewModel = { data -> ChapterItemArticleViewModel(data, theme) },
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
    createItemBinder<ItemText1Binding, HotKey, ItemTextHotkeyViewModel>(
        getItemViewModel = { data -> ItemTextHotkeyViewModel(data, theme) },
    )

fun itemText2HotkeyBinder(
    onItemListener: OnItemListener<ItemDataViewModel<HotKey>>,
    theme: AppDynamicTheme,
) = createItemBinder<ItemText2Binding, HotKey, ItemTextHotkeyViewModel>(
    getItemViewModel = { data -> ItemTextHotkeyViewModel(data, theme) },
    onItemListener = onItemListener,
)

fun itemArticleBinder(
    theme: AppDynamicTheme,
) = createItemBinder<ItemArticleBinding, ArticleBean, ArticleItemArticleViewModel>(
    getItemViewModel = { data -> ArticleItemArticleViewModel(data, theme) },
)

fun itemTextDeleteHotkeyBinder(
    onCreateViewModel: (model: ItemTextDeleteHotkeyViewModel) -> Unit = { _ -> },
    onItemListener: OnItemListener<ItemDataViewModel<HotKey>>? = null,
    onBindViewModel: (binding: ItemTextDeleteBinding, data: HotKey?, viewModel: ItemTextDeleteHotkeyViewModel?, posiion: Int) -> Boolean = { _, _, _, _ -> false },
    theme: AppDynamicTheme,
) = createItemBinder<ItemTextDeleteBinding, HotKey, ItemTextDeleteHotkeyViewModel>(
    getItemViewModel = { data ->
        ItemTextDeleteHotkeyViewModel(data, theme).apply(onCreateViewModel)
    },
    onItemListener = onItemListener,
    onBindViewModel = onBindViewModel
)

fun itemDeleteBarHotkeyBinder(
    onCreateViewModel: (model: ItemDeleteBarHotkeyViewModel) -> Unit = { _ -> },
    onItemListener: OnItemListener<ItemDataViewModel<HotKey>>? = null,
    onBindViewModel: (binding: ItemDeleteBarBinding, data: HotKey?, viewModel: ItemDeleteBarHotkeyViewModel?, posiion: Int) -> Boolean = { _, _, _, _ -> false },
    theme: AppDynamicTheme,
) = createItemBinder<ItemDeleteBarBinding, HotKey, ItemDeleteBarHotkeyViewModel>(
    getItemViewModel = { data ->
        ItemDeleteBarHotkeyViewModel(data, theme).apply(onCreateViewModel)
    },
    onItemListener = onItemListener,
    onBindViewModel = onBindViewModel
)

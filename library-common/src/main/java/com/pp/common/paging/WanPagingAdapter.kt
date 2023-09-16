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
        return oldItem == newItem
    }
}

fun itemTextArticleListBinder(
    onItemListener: OnItemListener<ItemDataViewModel<ArticleListBean>>? = null,
    onBindViewModel: (binding: ItemTextBinding, viewModel: ItemArticleListTextViewModel?, position: Int, getItem: (position: Int) -> ArticleListBean?) -> Boolean = { _, _, _, _ -> false },
    theme: AppDynamicTheme,
) = createItemBinder<ItemTextBinding, ArticleListBean, ItemArticleListTextViewModel>(
    getItemViewModel = { getItem -> ItemArticleListTextViewModel(getItem, theme) },
    onItemListener = onItemListener,
    onBindViewModel = onBindViewModel
)

fun itemText1ArticleListBinder(
    theme: AppDynamicTheme,
) = createItemBinder<ItemText1Binding, ArticleListBean, ItemArticleListTextViewModel>(
    getItemViewModel = { getItem -> ItemArticleListTextViewModel(getItem, theme) },
)


fun itemText2ArticleBinder(
    theme: AppDynamicTheme,
) = createItemBinder<ItemText2Binding, ArticleBean, ItemArticleTextViewModel>(
    getItemViewModel = { getItem -> ItemArticleTextViewModel(getItem, theme) },
)

fun itemText3ArticleBinder(
    onItemListener: OnItemListener<ItemDataViewModel<ArticleListBean>>? = null,
    onBindViewModel: (binding: ItemText3Binding, viewModel: ItemArticleListTextViewModel?, position: Int, getItem: (position: Int) -> ArticleListBean?) -> Boolean = { _, _, _, _ -> false },
    theme: AppDynamicTheme,
) = createItemBinder<ItemText3Binding, ArticleListBean, ItemArticleListTextViewModel>(
    getItemViewModel = { getItem -> ItemArticleListTextViewModel(getItem, theme) },
    onItemListener = onItemListener,
    onBindViewModel = onBindViewModel
)

fun itemText2ArticleListBinder(
    onItemListener: OnItemListener<ItemDataViewModel<ArticleListBean>>? = null,
    theme: AppDynamicTheme,
) = createItemBinder<ItemText2Binding, ArticleListBean, ItemArticleListTextViewModel>(
    getItemViewModel = { getItem -> ItemArticleListTextViewModel(getItem, theme) },
    onItemListener = onItemListener
)

fun itemWXArticleBinder(
    theme: AppDynamicTheme,
) = createItemBinder<ItemWxArticleBinding, ArticleBean, ChapterItemArticleViewModel>(
    getItemViewModel = { getItem -> ChapterItemArticleViewModel(getItem, theme) },
)

fun itemProjectArticleBinder(
    theme: AppDynamicTheme,
) = createItemBinder<ItemProjectarticleBinding, ArticleBean, ChapterItemArticleViewModel>(
    getItemViewModel = { getItem -> ChapterItemArticleViewModel(getItem, theme) },
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
        getItemViewModel = { getItem -> ItemTextHotkeyViewModel(getItem, theme) },
    )

fun itemText2HotkeyBinder(
    onItemListener: OnItemListener<ItemDataViewModel<HotKey>>,
    theme: AppDynamicTheme,
) = createItemBinder<ItemText2Binding, HotKey, ItemTextHotkeyViewModel>(
    getItemViewModel = { getItem -> ItemTextHotkeyViewModel(getItem, theme) },
    onItemListener = onItemListener,
)

fun itemArticleBinder(
    theme: AppDynamicTheme,
) = createItemBinder<ItemArticleBinding, ArticleBean, ArticleItemArticleViewModel>(
    getItemViewModel = { getItem -> ArticleItemArticleViewModel(getItem, theme) },
)

fun itemTextDeleteHotkeyBinder(
    onCreateViewModel: (model: ItemTextDeleteHotkeyViewModel) -> Unit = { _ -> },
    onItemListener: OnItemListener<ItemDataViewModel<HotKey>>? = null,
    onBindViewModel: (binding: ItemTextDeleteBinding, viewModel: ItemTextDeleteHotkeyViewModel?, position: Int, getItem: (pos: Int) -> HotKey?) -> Boolean = { _, _, _, _ -> false },
    theme: AppDynamicTheme,
) = createItemBinder<ItemTextDeleteBinding, HotKey, ItemTextDeleteHotkeyViewModel>(
    getItemViewModel = { getItem ->
        ItemTextDeleteHotkeyViewModel(getItem, theme).apply(onCreateViewModel)
    },
    onItemListener = onItemListener,
    onBindViewModel = onBindViewModel
)

fun itemDeleteBarHotkeyBinder(
    onCreateViewModel: (model: ItemDeleteBarHotkeyViewModel) -> Unit = { _ -> },
    onItemListener: OnItemListener<ItemDataViewModel<HotKey>>? = null,
    onBindViewModel: (binding: ItemDeleteBarBinding, viewModel: ItemDeleteBarHotkeyViewModel?, position: Int, getItem: (pos: Int) -> HotKey?) -> Boolean = { _, _, _, _ -> false },
    theme: AppDynamicTheme,
) = createItemBinder<ItemDeleteBarBinding, HotKey, ItemDeleteBarHotkeyViewModel>(
    getItemViewModel = { getItem ->
        ItemDeleteBarHotkeyViewModel(getItem, theme).apply(onCreateViewModel)
    },
    onItemListener = onItemListener,
    onBindViewModel = onBindViewModel
)

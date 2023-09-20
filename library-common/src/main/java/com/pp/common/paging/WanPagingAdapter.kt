package com.pp.common.paging

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.pp.common.http.wanandroid.bean.*
import com.pp.common.model.*
import com.pp.common.repository.CollectedRepository
import com.pp.theme.AppDynamicTheme
import com.pp.ui.R
import com.pp.ui.adapter.BindingPagingDataAdapter
import com.pp.ui.adapter.createItemDataBinder
import com.pp.ui.databinding.*
import com.pp.ui.viewModel.ItemDataViewModel
import com.pp.ui.viewModel.OnItemListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val articleDifferCallback = object : DiffUtil.ItemCallback<ArticleBean>() {
    override fun areItemsTheSame(oldItem: ArticleBean, newItem: ArticleBean): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ArticleBean, newItem: ArticleBean): Boolean {
        return oldItem == newItem
    }
}

val hotKeyDifferCallback = object : DiffUtil.ItemCallback<HotKeyBean>() {
    override fun areItemsTheSame(oldItem: HotKeyBean, newItem: HotKeyBean): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: HotKeyBean, newItem: HotKeyBean): Boolean {
        return oldItem == newItem
    }
}

val coinReasonDifferCallback = object : DiffUtil.ItemCallback<CoinReasonBean>() {
    override fun areItemsTheSame(oldItem: CoinReasonBean, newItem: CoinReasonBean): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CoinReasonBean, newItem: CoinReasonBean): Boolean {
        return oldItem == newItem
    }
}

val coinRankDifferCallback = object : DiffUtil.ItemCallback<CoinInfoBean>() {
    override fun areItemsTheSame(oldItem: CoinInfoBean, newItem: CoinInfoBean): Boolean {
        return oldItem.userId == newItem.userId
    }

    override fun areContentsTheSame(oldItem: CoinInfoBean, newItem: CoinInfoBean): Boolean {
        return oldItem == newItem
    }
}

fun itemTextArticleListBinder(
    onItemListener: OnItemListener<ItemDataViewModel<ArticleListBean>>? = null,
    onBindViewModel: (binding: ItemTextBinding, viewModel: ItemArticleListTextViewModel?, position: Int, data: ArticleListBean?) -> Boolean = { _, _, _, _ -> false },
    theme: AppDynamicTheme,
) = createItemDataBinder<ItemTextBinding, ArticleListBean, ItemArticleListTextViewModel>(
    getItemViewModel = { data -> ItemArticleListTextViewModel(data, theme) },
    onItemListener = onItemListener,
    onBindViewModel = onBindViewModel
)

fun itemText1ArticleListBinder(
    theme: AppDynamicTheme,
) = createItemDataBinder<ItemText1Binding, ArticleListBean, ItemArticleListTextViewModel>(
    getItemViewModel = { data -> ItemArticleListTextViewModel(data, theme) },
)


fun itemText2ArticleBinder(
    theme: AppDynamicTheme,
) = createItemDataBinder<ItemText2Binding, ArticleBean, ItemArticleTextViewModel>(
    getItemViewModel = { data -> ItemArticleTextViewModel(data, theme) },
)

fun itemText3ArticleBinder(
    onItemListener: OnItemListener<ItemDataViewModel<ArticleListBean>>? = null,
    onBindViewModel: (binding: ItemText3Binding, viewModel: ItemArticleListTextViewModel?, position: Int, data: ArticleListBean?) -> Boolean = { _, _, _, _ -> false },
    theme: AppDynamicTheme,
) = createItemDataBinder<ItemText3Binding, ArticleListBean, ItemArticleListTextViewModel>(
    getItemViewModel = { data -> ItemArticleListTextViewModel(data, theme) },
    onItemListener = onItemListener,
    onBindViewModel = onBindViewModel
)

fun itemText2ArticleListBinder(
    onItemListener: OnItemListener<ItemDataViewModel<ArticleListBean>>? = null,
    theme: AppDynamicTheme,
) = createItemDataBinder<ItemText2Binding, ArticleListBean, ItemArticleListTextViewModel>(
    getItemViewModel = { data -> ItemArticleListTextViewModel(data, theme) },
    onItemListener = onItemListener
)

fun itemWXArticleBinder(
    theme: AppDynamicTheme, scope: CoroutineScope,
) = createItemDataBinder<ItemWxArticleBinding, ArticleBean, ChapterItemArticleViewModel>(
    getItemViewModel = { data -> ChapterItemArticleViewModel(data, theme, scope) },
)

fun itemProjectArticleBinder(
    theme: AppDynamicTheme,
    scope: CoroutineScope,
) = createItemDataBinder<ItemProjectarticleBinding, ArticleBean, ChapterItemArticleViewModel>(
    getItemViewModel = { data -> ChapterItemArticleViewModel(data, theme, scope) },
)

fun itemArticlePagingAdapter(theme: AppDynamicTheme, scope: CoroutineScope) =
    BindingPagingDataAdapter<ArticleBean>({
        R.layout.item_article
    }, diffCallback = articleDifferCallback).apply {
        itemArticleBinder(theme, scope).also {
            addItemViewModelBinder(it)
        }

        collectedListener(scope)
    }

fun itemText1HotkeyBinder(theme: AppDynamicTheme) =
    createItemDataBinder<ItemText1Binding, HotKeyBean, ItemTextHotkeyViewModel>(
        getItemViewModel = { data -> ItemTextHotkeyViewModel(data, theme) },
    )

fun itemText2HotkeyBinder(
    onItemListener: OnItemListener<ItemDataViewModel<HotKeyBean>>,
    theme: AppDynamicTheme,
) = createItemDataBinder<ItemText2Binding, HotKeyBean, ItemTextHotkeyViewModel>(
    getItemViewModel = { data -> ItemTextHotkeyViewModel(data, theme) },
    onItemListener = onItemListener,
)

fun itemArticleBinder(
    theme: AppDynamicTheme,
    scope: CoroutineScope,
) = createItemDataBinder<ItemArticleBinding, ArticleBean, ArticleItemArticleViewModel>(
    getItemViewModel = { data -> ArticleItemArticleViewModel(data, theme, scope) },
)

fun itemArticleCollectedBinder(
    theme: AppDynamicTheme,
    scope: CoroutineScope,
    onItemListener: OnItemListener<ItemDataViewModel<ArticleBean>>? = null,
) = createItemDataBinder<ItemArticleCollectedBinding, ArticleBean, ChapterItemArticleViewModel>(
    getItemViewModel = { data -> ChapterItemArticleViewModel(data, theme, scope) },
    onItemListener = onItemListener
)

fun itemTextDeleteHotkeyBinder(
    onCreateViewModel: (model: ItemTextDeleteHotkeyViewModel) -> Unit = { _ -> },
    onItemListener: OnItemListener<ItemDataViewModel<HotKeyBean>>? = null,
    onBindViewModel: (binding: ItemTextDeleteBinding, viewModel: ItemTextDeleteHotkeyViewModel?, position: Int, data: HotKeyBean?) -> Boolean = { _, _, _, _ -> false },
    theme: AppDynamicTheme,
) = createItemDataBinder<ItemTextDeleteBinding, HotKeyBean, ItemTextDeleteHotkeyViewModel>(
    getItemViewModel = { data ->
        ItemTextDeleteHotkeyViewModel(data, theme).apply(onCreateViewModel)
    },
    onItemListener = onItemListener,
    onBindViewModel = onBindViewModel
)

fun itemDeleteBarHotkeyBinder(
    onCreateViewModel: (model: ItemDeleteBarHotkeyViewModel) -> Unit = { _ -> },
    onItemListener: OnItemListener<ItemDataViewModel<HotKeyBean>>? = null,
    onBindViewModel: (binding: ItemDeleteBarBinding, viewModel: ItemDeleteBarHotkeyViewModel?, position: Int, data: HotKeyBean?) -> Boolean = { _, _, _, _ -> false },
    theme: AppDynamicTheme,
) = createItemDataBinder<ItemDeleteBarBinding, HotKeyBean, ItemDeleteBarHotkeyViewModel>(
    getItemViewModel = { data ->
        ItemDeleteBarHotkeyViewModel(data, theme).apply(onCreateViewModel)
    },
    onItemListener = onItemListener,
    onBindViewModel = onBindViewModel
)

fun itemCoinReasonBinder(
    theme: AppDynamicTheme,
) = createItemDataBinder<ItemCoinReasonBinding, CoinReasonBean, ItemCoinReasonItemViewModel>(
    getItemViewModel = { data ->
        ItemCoinReasonItemViewModel(data, theme)
    }
)

fun itemCoinRankBinder(
    theme: AppDynamicTheme,
) = createItemDataBinder<ItemCoinRankBinding, CoinInfoBean, ItemCoinRankItemViewModel>(
    getItemViewModel = { data ->
        ItemCoinRankItemViewModel(data, theme)
    }
)

/**
 * 收藏监听,刷新adapter对应 item data
 */
fun BindingPagingDataAdapter<ArticleBean>.collectedListener(scope: CoroutineScope) {
    scope.launch {
        CollectedRepository.collected.collect { collectBean ->
            update {
                if (it?.id == collectBean.originId || it?.id == collectBean.id) {
                    it?.collect = collectBean.collect
                    true
                } else {
                    false
                }
            }
        }
    }
}

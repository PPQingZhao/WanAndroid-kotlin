package com.pp.common.paging

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.ArticleListBean
import com.pp.common.model.*
import com.pp.theme.AppDynamicTheme
import com.pp.ui.adapter.BindingPagingDataAdapter
import com.pp.ui.adapter.DefaultViewDataBindingItemType
import com.pp.ui.databinding.*
import com.pp.ui.viewModel.ItemDataViewModel


val articleDifferCallback = object : DiffUtil.ItemCallback<ArticleBean>() {
    override fun areItemsTheSame(oldItem: ArticleBean, newItem: ArticleBean): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ArticleBean, newItem: ArticleBean): Boolean {
        return oldItem == newItem
    }
}

fun <VB : ViewDataBinding, Data : Any, VM : ItemDataViewModel<Data>> createBindItemType(
    itemType: Int = 0,
    createBinding: (parent: ViewGroup) -> VB,
    onCreateViewModel: (data: Data?) -> VM,
    onBindItemViewModel: (bind: VB, viewModel: VM, position: Int) -> Unit = { _, _, _ -> },
) = DefaultViewDataBindingItemType<VB, VM, Data>(
    createBinding = { createBinding.invoke(it) },
    onBindItemViewModel = { bind, data, position, cacheViewModel ->
        (cacheViewModel?.also {
            it.data = data
        } ?: onCreateViewModel.invoke(data)).apply {
            onBindItemViewModel.invoke(bind, this, position)
        }
    },
    getItemType = { itemType },
)

fun itemTextArticleListBindItemType(
    itemType: Int = 0,
    inflater: LayoutInflater,
    theme: AppDynamicTheme,
    onBindItemViewModel: (bind: ItemTextBinding, viewModel: ItemArticleListTextViewModel, position: Int) -> Unit = { _, _, _ -> },
) = createBindItemType<ItemTextBinding, ArticleListBean, ItemArticleListTextViewModel>(
    itemType = itemType,
    createBinding = { ItemTextBinding.inflate(inflater, it, false) },
    onCreateViewModel = { ItemArticleListTextViewModel(it, theme) },
    onBindItemViewModel = onBindItemViewModel
)

fun itemText1ArticleListBindItemType(
    itemType: Int = 0,
    inflater: LayoutInflater,
    theme: AppDynamicTheme,
    onBindItemViewModel: (bind: ItemText1Binding, viewModel: ItemArticleListTextViewModel, position: Int) -> Unit = { _, _, _ -> },
) = createBindItemType<ItemText1Binding, ArticleListBean, ItemArticleListTextViewModel>(
    itemType = itemType,
    createBinding = { ItemText1Binding.inflate(inflater, it, false) },
    onCreateViewModel = { ItemArticleListTextViewModel(it, theme) },
    onBindItemViewModel = onBindItemViewModel
)

fun itemArticleText2BindItemType(
    itemType: Int = 0,
    inflater: LayoutInflater,
    theme: AppDynamicTheme,
    onBindItemViewModel: (bind: ItemText2Binding, viewModel: ItemArticleTextViewModel, position: Int) -> Unit = { _, _, _ -> },
) = createBindItemType<ItemText2Binding, ArticleBean, ItemArticleTextViewModel>(
    itemType = itemType,
    createBinding = { ItemText2Binding.inflate(inflater, it, false) },
    onCreateViewModel = { ItemArticleTextViewModel(it, theme) },
    onBindItemViewModel = onBindItemViewModel
)


fun itemText3ArticleBindItemType(
    itemType: Int = 0,
    inflater: LayoutInflater,
    theme: AppDynamicTheme,
    onBindItemViewModel: (bind: ItemText3Binding, viewModel: ItemArticleListTextViewModel, position: Int) -> Unit = { _, _, _ -> },
) = createBindItemType<ItemText3Binding, ArticleListBean, ItemArticleListTextViewModel>(
    itemType = itemType,
    createBinding = { ItemText3Binding.inflate(inflater, it, false) },
    onCreateViewModel = { ItemArticleListTextViewModel(it, theme) },
    onBindItemViewModel = onBindItemViewModel
)

fun itemText2ArticleListBindItemType(
    itemType: Int = 0,
    inflater: LayoutInflater,
    theme: AppDynamicTheme,
    onBindItemViewModel: (ItemText2Binding, ItemArticleListTextViewModel, Int) -> Unit = { _, _, _ -> },
) = createBindItemType<ItemText2Binding, ArticleListBean, ItemArticleListTextViewModel>(
    itemType = itemType,
    createBinding = { ItemText2Binding.inflate(inflater, it, false) },
    onCreateViewModel = { ItemArticleListTextViewModel(it, theme) },
    onBindItemViewModel = onBindItemViewModel
)

fun itemArticleBindItemType(
    itemType: Int = 0,
    inflater: LayoutInflater,
    theme: AppDynamicTheme,
    onBindItemViewModel: (bind: ItemArticleBinding, viewModel: ArticleItemArticleViewModel, position: Int) -> Unit = { _, _, _ -> },
) = createBindItemType<ItemArticleBinding, ArticleBean, ArticleItemArticleViewModel>(
    itemType = itemType,
    createBinding = { ItemArticleBinding.inflate(inflater, it, false) },
    onCreateViewModel = { ArticleItemArticleViewModel(it, theme) },
    onBindItemViewModel = onBindItemViewModel
)

fun itemChapterArticleBindItemType(
    itemType: Int = 0,
    inflater: LayoutInflater,
    theme: AppDynamicTheme,
    onBindItemViewModel: (bind: ItemArticleBinding, viewModel: ChapterItemArticleViewModel, position: Int) -> Unit = { _, _, _ -> },
) = createBindItemType<ItemArticleBinding, ArticleBean, ChapterItemArticleViewModel>(
    itemType = itemType,
    createBinding = { ItemArticleBinding.inflate(inflater, it, false) },
    onCreateViewModel = { ChapterItemArticleViewModel(it, theme) },
    onBindItemViewModel = onBindItemViewModel
)

fun itemArticlePagingAdapter(layoutInflater: LayoutInflater, theme: AppDynamicTheme) =
    BindingPagingDataAdapter.RecyclerViewBindingAdapterImpl(
        bindingItemType = itemArticleBindItemType(
            inflater = layoutInflater,
            theme = theme
        ),
        diffCallback = articleDifferCallback
    )

fun itemChapterArticlePagingAdapter(
    layoutInflater: LayoutInflater,
    theme: AppDynamicTheme,
    onBindItemViewModel: (ItemArticleBinding, ChapterItemArticleViewModel, Int) -> Unit = { _, _, _ -> },
) =
    BindingPagingDataAdapter.RecyclerViewBindingAdapterImpl(
        bindingItemType = itemChapterArticleBindItemType(
            inflater = layoutInflater,
            theme = theme,
            onBindItemViewModel = onBindItemViewModel
        ),
        diffCallback = articleDifferCallback
    )

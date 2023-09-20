package com.pp.navigation.ui

import android.app.Application
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.recyclerview.widget.DividerItemDecoration
import com.pp.base.ThemeViewModel
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.ArticleListBean
import com.pp.common.model.ItemSelectedModel
import com.pp.common.paging.articleDifferCallback
import com.pp.common.paging.collectedListener
import com.pp.common.paging.itemText3ArticleBinder
import com.pp.navigation.repository.WXArticleRepository
import com.pp.ui.R
import com.pp.ui.adapter.BindingPagingDataAdapter
import com.pp.ui.adapter.RecyclerViewBindingAdapter
import com.pp.ui.viewModel.ItemDataViewModel
import com.pp.ui.viewModel.ItemTextViewModel
import com.pp.ui.viewModel.OnItemListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WXArticleViewModel(app: Application) : ThemeViewModel(app) {


    val dividerItemDecoration = DividerItemDecoration(app, DividerItemDecoration.VERTICAL)

    private val _wxArticleList = MutableSharedFlow<List<ArticleListBean>>()
    val wxArticleList = _wxArticleList.asSharedFlow()
    fun getWXArticleList() {
        viewModelScope.launch {
            WXArticleRepository.getWXArticleList().data.let {
                _wxArticleList.emit(it ?: emptyList())
            }
        }
    }

    private fun getWXArticle(id: Int): Flow<PagingData<ArticleBean>> {
        return WXArticleRepository.getWXArticle(id).cachedIn(viewModelScope)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        viewModelScope.launch {
            wxArticleList.collectLatest {
                mAdapter.setDataList(it)
            }
        }

        selectedItem.observerSelectedItem(
            owner
        ) {
            it?.run {

                if (pagingDataAdapter.itemCount > 0) {
                    pagingDataAdapter.clear()
                }
                viewModelScope.launch(Dispatchers.IO) {
                    getWXArticle(data?.id ?: 0).collectLatest {
                        pagingDataAdapter.setPagingData(viewModelScope, it)
                    }
                }
            }
        }
    }

    override fun onFirstResume(owner: LifecycleOwner) {
        getWXArticleList()
    }

    private val selectedItem: ItemSelectedModel<ArticleListBean, ItemTextViewModel<ArticleListBean>> =
        ItemSelectedModel()

    private val mOnItemListener = object :
        OnItemListener<ItemDataViewModel<ArticleListBean>> {
        override fun onItemClick(
            view: View,
            item: ItemDataViewModel<ArticleListBean>,
        ): Boolean {
            selectedItem.selectedItem(item as ItemTextViewModel<ArticleListBean>)
            return true
        }
    }

    val mAdapter by lazy {

        RecyclerViewBindingAdapter<ArticleListBean>(getItemLayoutRes = { R.layout.item_text3 })
            .apply {
                itemText3ArticleBinder(
                    onItemListener = mOnItemListener,
                    theme = mTheme,
                    onBindViewModel = { _, viewModel, position, _ ->
                        if (selectedItem.getSelectedItem() == null && position == 0) {
                            selectedItem.selectedItem(viewModel)
                        }
                        false
                    }).also {
                    addItemViewModelBinder(it)
                }
            }
    }

    val pagingDataAdapter by lazy {

        BindingPagingDataAdapter<ArticleBean>(
            { R.layout.item_wx_article },
            diffCallback = articleDifferCallback
        ).apply {
            com.pp.common.paging.itemWXArticleBinder(mTheme, viewModelScope)
                .also {
                    addItemViewModelBinder(it)
                }

            collectedListener(viewModelScope)
        }

    }
}
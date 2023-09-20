package com.pp.user.ui

import android.app.Application
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.pp.base.ThemeViewModel
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.paging.articleDifferCallback
import com.pp.common.paging.itemArticleCollectedBinder
import com.pp.common.repository.CollectedRepository
import com.pp.common.router.MultiRouterFragmentViewModel
import com.pp.common.util.ViewTreeMultiRouterFragmentViewModel
import com.pp.router_service.RouterPath
import com.pp.ui.R
import com.pp.ui.adapter.BindingPagingDataAdapter
import com.pp.ui.viewModel.ItemDataViewModel
import com.pp.ui.viewModel.OnItemListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CollectedViewModel(app: Application) : ThemeViewModel(app) {

    val mAdapter by lazy {
        BindingPagingDataAdapter<ArticleBean>({
            R.layout.item_article_collected
        }, diffCallback = articleDifferCallback).apply {
            itemArticleCollectedBinder(
                theme = mTheme,
                scope = viewModelScope,
                onItemListener = object : OnItemListener<ItemDataViewModel<ArticleBean>> {
                    override fun onItemClick(
                        view: View,
                        item: ItemDataViewModel<ArticleBean>,
                    ): Boolean {
                        if (R.id.tv_uncollected != view.id) return false
                        val articleBean = item.data ?: return false

                        viewModelScope.launch(Dispatchers.IO) {
                            // 取消收藏
                            // item刷新 -- 通过监听 CollectedRepository.getCollectedPageData().collectLatest {} 处理
                            CollectedRepository.unCollected2(articleBean)
                        }
                        return true
                    }
                }
            ).also {
                addItemViewModelBinder(it)
            }
        }
    }

    override fun onCreate(owner: LifecycleOwner) {
        viewModelScope.launch {
            // 监听 收藏/取消收藏
            // 在收藏页面只能取消收藏,所以直接删除已被取消收藏的item
            CollectedRepository.collected.collectLatest {
                mAdapter.remove(it)
            }
        }
    }

    // 获取收藏列表
    private fun loadCollectedList() {
        viewModelScope.launch(Dispatchers.IO) {
            CollectedRepository.getCollectedPageData().cachedIn(viewModelScope).collectLatest {
                mAdapter.setPagingData(this, it)
            }
        }
    }

    override fun onFirstResume(owner: LifecycleOwner) {
        loadCollectedList()
    }

    /**
     * 返回按钮点击事件
     */
    fun onBack(view: View) {
        ViewTreeMultiRouterFragmentViewModel.get<MultiRouterFragmentViewModel>(
            view
        )?.run {
            popBackStack(RouterPath.User.fragment_collected)
        }
    }
}
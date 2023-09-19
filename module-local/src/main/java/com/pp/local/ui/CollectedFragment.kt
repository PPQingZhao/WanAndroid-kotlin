package com.pp.local.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.transition.MaterialSharedAxis
import com.pp.base.ThemeFragment
import com.pp.common.http.wanandroid.api.WanAndroidService
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.paging.articleDifferCallback
import com.pp.common.paging.itemArticleCollectedBinder
import com.pp.common.util.materialSharedAxis
import com.pp.local.databinding.FragmentCollectedBinding
import com.pp.router_service.RouterPath
import com.pp.ui.R
import com.pp.ui.adapter.BindingPagingDataAdapter
import com.pp.ui.utils.StateView
import com.pp.ui.utils.attachStateView
import com.pp.ui.utils.setPagingAdapter
import com.pp.ui.viewModel.ItemDataViewModel
import com.pp.ui.viewModel.OnItemListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Route(path = RouterPath.Local.fragment_collected)
class CollectedFragment : ThemeFragment<FragmentCollectedBinding, CollectedViewModel>() {
    override val mBinding by lazy {
        FragmentCollectedBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<CollectedViewModel> {
        return CollectedViewModel::class.java
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = materialSharedAxis(MaterialSharedAxis.X, true)
        exitTransition = materialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPagingList()
    }

    private val mAdapter by lazy {
        BindingPagingDataAdapter<ArticleBean>({
            R.layout.item_article_collected
        }, diffCallback = articleDifferCallback).apply {
            itemArticleCollectedBinder(
                theme = mViewModel.mTheme,
                onItemListener = object : OnItemListener<ItemDataViewModel<ArticleBean>> {
                    override fun onItemClick(
                        view: View,
                        item: ItemDataViewModel<ArticleBean>,
                    ): Boolean {
                        if (R.id.tv_uncollected != view.id) return false
                        val articleBean = item.data ?: return false

                        viewLifecycleOwner.lifecycleScope.launch {
                            // 取消收藏
                            withContext(Dispatchers.IO) {
                                mViewModel.unCollected(articleBean.id, articleBean.originId ?: -1)
                            }.let {
                                if (it.errorCode != WanAndroidService.ErrorCode.SUCCESS) {
                                    return@launch
                                }
                                // 删除item
                                remove(articleBean)
                            }
                        }
                        return true
                    }
                }
            ).also {
                addItemViewModelBinder(it)
            }
        }
    }

    private fun initPagingList() {
        viewLifecycleOwner.lifecycleScope.launch {

            StateView.DefaultBuilder(
                mBinding.refreshLayout, mViewModel.mTheme, viewLifecycleOwner
            )
                .setOnRetry { mAdapter.refresh() }
                .build()
                .also {
                    mAdapter.attachStateView(it)
                }

            mBinding.recyclerview.setPagingAdapter(
                lifecycleScope = viewLifecycleOwner.lifecycleScope,
                pageData = mViewModel.getCollectedPageData(),
                pagingAdapter = mAdapter,
                layoutManager = LinearLayoutManager(requireContext()),
                refreshLayout = mBinding.refreshLayout
            )
        }
    }

    override fun onFirstResume() {
        initPagingList()
    }
}
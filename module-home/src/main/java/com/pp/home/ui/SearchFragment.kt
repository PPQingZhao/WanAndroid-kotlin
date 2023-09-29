package com.pp.home.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.doOnNextLayout
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.transition.MaterialSharedAxis
import com.pp.base.ThemeFragment
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.paging.articleDifferCallback
import com.pp.common.paging.collectedListener
import com.pp.common.paging.itemArticleBinder
import com.pp.common.router.MultiRouterFragmentViewModel
import com.pp.common.util.materialSharedAxis
import com.pp.home.databinding.FragmentSearchBinding
import com.pp.router_service.RouterPath
import com.pp.ui.adapter.BindingPagingDataAdapter
import com.pp.ui.utils.StateView
import com.pp.ui.utils.attachRecyclerView
import com.pp.ui.utils.attachRefreshView
import com.pp.ui.utils.attachStateView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Route(path = RouterPath.Search.fragment_search)
class SearchFragment : ThemeFragment<FragmentSearchBinding, SearchViewModel>() {
    override val mBinding by lazy {
        FragmentSearchBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<SearchViewModel> {
        return SearchViewModel::class.java
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enterTransition = materialSharedAxis(MaterialSharedAxis.Z, true)
        exitTransition = materialSharedAxis(MaterialSharedAxis.Z, false)

        mViewModel.searchText.observe(viewLifecycleOwner) {
            mBinding.searchView.setQuery(it, true)
        }

        initView()
        initSearchView()
        initSearchRecyclerView()
    }


    private val mSearchAdapter by lazy {

        BindingPagingDataAdapter<ArticleBean>(
            { com.pp.ui.R.layout.item_article },
            diffCallback = articleDifferCallback
        ).apply {
            itemArticleBinder(mViewModel.mTheme, mViewModel.viewModelScope).also {
                addItemViewModelBinder(it)
            }

            collectedListener(viewLifecycleOwner.lifecycleScope)

            StateView.DefaultBuilder(mBinding.refreshLayout, mViewModel.mTheme, viewLifecycleOwner)
                .setOnRetry {
                    refresh()
                }.build()
                .let {
                    attachStateView(it)
                }
        }
    }

    private fun initSearchRecyclerView() {
        mSearchAdapter.attachRecyclerView(mBinding.searchRecyclerview, LinearLayoutManager(context))
        mSearchAdapter.attachRefreshView(mBinding.refreshLayout)
    }

    private fun initSearchView() {
        mBinding.searchView.isSubmitButtonEnabled = true

        mBinding.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query?.isNotBlank() != true) {
                    return true
                }

                mBinding.searchView.clearFocus()
                mViewModel.saveSearchHotKeyHistory(query)
                viewLifecycleOwner.lifecycleScope.launch {
                    if (mSearchAdapter.itemCount > 0) {
                        mSearchAdapter.clear()
                    }
                    kotlin.runCatching {
                        mViewModel.searchPageData(query).collectLatest {

                            mBinding.refreshLayout.visibility = View.VISIBLE

                            mBinding.searchRecyclerview.scrollToPosition(0)

                            withContext(Dispatchers.IO) {
                                mSearchAdapter.setPagingData(this, it)
                            }
                        }
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.isNotEmpty() != true) {
                    if (mSearchAdapter.itemCount > 0) {
                        mSearchAdapter.clear()
                    }
                    mBinding.searchRecyclerview.doOnNextLayout {
                        mBinding.refreshLayout.visibility = View.GONE
                    }
                }
                return true
            }
        })

    }

    private fun initView() {
        mBinding.ivBack.setOnClickListener {
            mBinding.searchView.clearFocus()

            MultiRouterFragmentViewModel.popBackStack(mBinding.root, RouterPath.Search.fragment_search)

        }
    }

}
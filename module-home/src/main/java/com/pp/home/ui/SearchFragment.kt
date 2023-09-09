package com.pp.home.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.doOnNextLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.transition.MaterialSharedAxis
import com.pp.base.ThemeFragment
import com.pp.common.app.App
import com.pp.common.constant.ON_BACK_PRESSED
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.HotKey
import com.pp.common.paging.articleDifferCallback
import com.pp.common.paging.itemArticleBinder
import com.pp.common.paging.itemText1HotkeyBinder
import com.pp.common.paging.itemText2HotkeyBinder
import com.pp.common.util.materialSharedAxis
import com.pp.home.databinding.FragmentSearchBinding
import com.pp.router_service.RouterPath
import com.pp.ui.adapter.BindingPagingDataAdapter
import com.pp.ui.viewModel.ItemDataViewModel
import com.pp.ui.viewModel.OnItemListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
        initView()
        initSearchView()
        initHistoryRecyclerView()
        initHotkeyRecyclerView()
        initSearchRecyclerView()

    }

    private val mHistoryAdapter by lazy {
        createFlexBoxAdapter()
    }

    private fun initHistoryRecyclerView() {
        mBinding.historyRecyclerview.layoutManager = FlexboxLayoutManager(requireContext()).apply {
        }
        mBinding.historyRecyclerview.adapter = mHistoryAdapter
    }

    private val mSearchAdapter by lazy {

        BindingPagingDataAdapter<ArticleBean>(
            { com.pp.ui.R.layout.item_article },
            diffCallback = articleDifferCallback
        ).apply {
            itemArticleBinder(mViewModel.mTheme).also {
                addItemViewModelBinder(it)
            }
        }
    }

    private fun initSearchRecyclerView() {
        mBinding.searchRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        mBinding.searchRecyclerview.adapter = mSearchAdapter
    }

    private fun createFlexBoxAdapter(): BindingPagingDataAdapter<HotKey> {
        val onItemListener = object : OnItemListener<ItemDataViewModel<HotKey>> {
            override fun onItemClick(view: View, item: ItemDataViewModel<HotKey>): Boolean {
                item.data?.name.let {
                    mBinding.searchView.setQuery(it, true)
                }
                return true
            }
        }

        val differCallback = object : DiffUtil.ItemCallback<HotKey>() {
            override fun areItemsTheSame(oldItem: HotKey, newItem: HotKey): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: HotKey, newItem: HotKey): Boolean {
                return oldItem == newItem
            }

        }

        return BindingPagingDataAdapter<HotKey>(
            {
                if (it is HotKey && it.id >= 0) {
                    com.pp.ui.R.layout.item_text2
                } else {
                    com.pp.ui.R.layout.item_text1
                }
            },
            diffCallback = differCallback
        ).apply {
            itemText1HotkeyBinder(mViewModel.mTheme).also {
                addItemViewModelBinder(it)
            }

            itemText2HotkeyBinder(onItemListener, mViewModel.mTheme).also {
                addItemViewModelBinder(it)
            }
        }
    }

    private val mHotkeyAdapter by lazy {
        createFlexBoxAdapter()
    }

    private fun initHotkeyRecyclerView() {
        mBinding.keyRecyclerview.layoutManager = FlexboxLayoutManager(requireContext()).apply {
            isAutoMeasureEnabled = false
        }
        mBinding.keyRecyclerview.adapter = mHotkeyAdapter
    }

    private fun initSearchView() {
        mBinding.searchView.isSubmitButtonEnabled = true

        mBinding.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query?.isNotBlank() != true) {
                    return true
                }

                mViewModel.saveSearchHotKeyHistory(query)
                mBinding.searchView.clearFocus()
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    mViewModel.searchPageData(query).collectLatest {
                        mBinding.searchRecyclerview.post {
                            mBinding.searchRecyclerview.visibility = View.VISIBLE
                        }
                        mSearchAdapter.setPagingData(this, it)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.isNotEmpty() != true) {
                    mSearchAdapter.clear()
                    mBinding.searchRecyclerview.doOnNextLayout {
                        mBinding.searchRecyclerview.visibility = View.GONE
                    }
                }
                return true
            }
        })

    }

    private fun initView() {
        mBinding.ivBack.setOnClickListener {
            mBinding.searchView.clearFocus()
            App.getInstance().navigation.value = ON_BACK_PRESSED to Any()
        }
    }

    override fun onFirstResume() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            async {
                mViewModel.getSearchHot().collectLatest {
                    mHotkeyAdapter.setPagingData(this, it)
                }
            }

            async {
                mViewModel.getSearchHotkeyHistory().collectLatest {
                    mHistoryAdapter.setData(this, it)
                }
            }

        }
    }
}
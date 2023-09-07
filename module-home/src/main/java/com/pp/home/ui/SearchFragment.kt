package com.pp.home.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.transition.MaterialSharedAxis
import com.pp.base.ThemeFragment
import com.pp.common.app.App
import com.pp.common.constant.Constants
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.HotKey
import com.pp.common.paging.itemArticleBinder
import com.pp.common.paging.itemText1HotkeyBinder
import com.pp.common.paging.itemText2HotkeyBinder
import com.pp.common.util.materialSharedAxis
import com.pp.home.databinding.FragmentSearchBinding
import com.pp.router_service.RouterPath
import com.pp.ui.adapter.BindingPagingDataAdapter2
import com.pp.ui.viewModel.ItemDataViewModel
import com.pp.ui.viewModel.OnItemListener
import kotlinx.coroutines.Dispatchers
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
        initHotkeyRecyclerView()
        initSearchRecyclerView()
    }

    private val mSearchAdapter by lazy {

        BindingPagingDataAdapter2<ArticleBean>({
            com.pp.ui.R.layout.item_article
        }).apply {
            itemArticleBinder(mViewModel.mTheme).also {
                addItemViewModelBinder(it)
            }
        }
    }

    private fun initSearchRecyclerView() {
        mBinding.searchRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        mBinding.searchRecyclerview.adapter = mSearchAdapter
    }

    private val mHotkeyAdapter by lazy {

        val onItemListener = object : OnItemListener<ItemDataViewModel<HotKey>> {
            override fun onItemClick(view: View, item: ItemDataViewModel<HotKey>): Boolean {
                item.data?.name.let {
                    mBinding.searchView.setQuery(it, true)
                }
                return true
            }
        }

        BindingPagingDataAdapter2<HotKey>({
            if (it is HotKey && it.id > 0) {
                com.pp.ui.R.layout.item_text2
            } else {
                com.pp.ui.R.layout.item_text1
            }
        }).apply {
            itemText1HotkeyBinder(mViewModel.mTheme).also {
                addItemViewModelBinder(it)
            }

            itemText2HotkeyBinder(onItemListener, mViewModel.mTheme).also {
                addItemViewModelBinder(it)
            }

        }
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
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    mViewModel.searchPageData(query).collectLatest {
                        mBinding.searchRecyclerview.post {
                            mBinding.searchRecyclerview.visibility = View.VISIBLE
                        }
                        mSearchAdapter.submitData(it)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.isNotEmpty() != true) {
                    mBinding.searchRecyclerview.visibility = View.GONE
                }
                return true
            }
        })

    }

    private fun initView() {
        mBinding.ivBack.setOnClickListener {
            mBinding.searchView.clearFocus()
            App.getInstance().navigation.value = Constants.ON_BACK_PRESSED to Any()
        }
    }

    override fun onFirstResume() {
        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.getSearchHot().collectLatest {
                mHotkeyAdapter.submitData(it)
            }
        }
    }
}
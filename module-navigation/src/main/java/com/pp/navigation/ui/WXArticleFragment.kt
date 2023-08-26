package com.pp.navigation.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pp.base.ThemeFragment
import com.pp.common.http.wanandroid.bean.ArticleListBean
import com.pp.common.paging.itemChapterArticlePagingAdapter
import com.pp.navigation.databinding.FragmentWxarticleBinding
import com.pp.navigation.model.ItemArticleListTextViewModel
import com.pp.ui.adapter.RecyclerViewBindingAdapter
import com.pp.ui.databinding.ItemText3Binding
import com.pp.ui.utils.setPagingAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WXArticleFragment private constructor() :
    ThemeFragment<FragmentWxarticleBinding, WXArticleViewModel>() {
    override val mBinding: FragmentWxarticleBinding by lazy {
        FragmentWxarticleBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<WXArticleViewModel> {
        return WXArticleViewModel::class.java
    }

    companion object {
        fun newInstance(): WXArticleFragment {
            return WXArticleFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerview()
    }


    private val mAdapter =
        RecyclerViewBindingAdapter.DefaultRecyclerViewBindingAdapter<ItemText3Binding, ItemArticleListTextViewModel, ArticleListBean>(
            onCreateBinding = {
                ItemText3Binding.inflate(layoutInflater, it, false)
            },
            onBindItemModel = { _, data, position, cacheItemViewModel ->
                if (cacheItemViewModel is ItemArticleListTextViewModel) {
                    cacheItemViewModel.also { it.data = data }
                } else {
                    ItemArticleListTextViewModel(data, mViewModel.mTheme)
                }.apply {
                    if (ItemArticleListTextViewModel.getSelectedItem() == null && position == 0) {
                        ItemArticleListTextViewModel.selectedItem(this)
                    }
                }
            }

        )

    private fun initRecyclerview() {
        mBinding.authorRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL).let {
            mBinding.authorRecyclerview.addItemDecoration(it)
        }
        mBinding.authorRecyclerview.adapter = mAdapter

        mBinding.wxarticleRecyclerview.layoutManager = LinearLayoutManager(requireContext())


        ItemArticleListTextViewModel.observerSelectedItem(
            viewLifecycleOwner
        ) {
            it?.run {
                mBinding.wxarticleRecyclerview.setPagingAdapter(
                    viewLifecycleOwner,
                    lifecycleScope,
                    mViewModel.getWXArticle(this.data?.id ?: 0),
                    itemChapterArticlePagingAdapter(layoutInflater, mViewModel.mTheme)
                )
            }
        }
    }

    override fun onFirstResume() {
        lifecycleScope.launch {
            mViewModel.wxArticleList.collectLatest {
                mAdapter.setDataList(it)
            }

        }
        mViewModel.getWXArticleList()
    }


}
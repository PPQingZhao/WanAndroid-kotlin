package com.pp.home.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnAttach
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pp.base.ThemeFragment
import com.pp.common.paging.itemArticlePagingAdapter
import com.pp.home.databinding.FragmentHomeChildSquareBinding
import com.pp.ui.utils.StateView
import com.pp.ui.utils.attachStateView
import com.pp.ui.utils.setPagingAdapter

class SquareFragment : ThemeFragment<FragmentHomeChildSquareBinding, SquareViewModel>() {

    override val mBinding by lazy { FragmentHomeChildSquareBinding.inflate(layoutInflater) }

    override fun getModelClazz(): Class<SquareViewModel> {
        return SquareViewModel::class.java
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStateView()
    }

    private val mArticleAdapter by lazy {
        itemArticlePagingAdapter(mViewModel.mTheme, mViewModel.viewModelScope)
    }

    private fun initStateView() {
        mBinding.refreshLayout.doOnAttach {

            StateView.DefaultBuilder(mBinding.refreshLayout, mViewModel.mTheme, viewLifecycleOwner)
                .setOnRetry {

                    mArticleAdapter.refresh()
                }
                .build()
                .also {
                    mArticleAdapter.attachStateView(it)
                }
        }

    }

    private fun initPagingList() {

        mBinding.pageListView.setPagingAdapter(
            lifecycleScope,
            mViewModel.getPageData(),
            mArticleAdapter,
            layoutManager = LinearLayoutManager(requireContext()),
            refreshLayout = mBinding.refreshLayout
        )
    }

    override fun onFirstResume() {
        super.onFirstResume()
        initPagingList()
    }

}
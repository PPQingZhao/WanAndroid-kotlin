package com.pp.home.ui

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pp.base.ThemeFragment
import com.pp.common.paging.itemArticlePagingAdapter
import com.pp.home.databinding.FragmentHomeChildSquareBinding
import com.pp.ui.utils.setPagingAdapter

class SquareFragment : ThemeFragment<FragmentHomeChildSquareBinding, SquareViewModel>() {

    override val mBinding by lazy { FragmentHomeChildSquareBinding.inflate(layoutInflater) }

    override fun getModelClazz(): Class<SquareViewModel> {
        return SquareViewModel::class.java
    }

    private fun initPagingList() {

        mBinding.pageListView.setPagingAdapter(
            lifecycleScope,
            mViewModel.getPageData(),
            itemArticlePagingAdapter(mViewModel.mTheme),
            layoutManager = LinearLayoutManager(requireContext()),
            refreshLayout = mBinding.refreshLayout
        )
    }

    override fun onFirstResume() {
        super.onFirstResume()
        initPagingList()
    }

}
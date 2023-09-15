package com.pp.home.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pp.base.ThemeFragment
import com.pp.common.paging.itemArticlePagingAdapter
import com.pp.home.databinding.FragmentHomeChildAnswerBinding
import com.pp.ui.utils.setPagingAdapter

class AnswerFragment : ThemeFragment<FragmentHomeChildAnswerBinding, AnswerViewModel>() {
    override val mBinding by lazy { FragmentHomeChildAnswerBinding.inflate(layoutInflater) }
    override fun getModelClazz(): Class<AnswerViewModel> {
        return AnswerViewModel::class.java
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
package com.pp.home.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnAttach
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pp.base.ThemeFragment
import com.pp.common.paging.itemArticlePagingAdapter
import com.pp.home.databinding.FragmentHomeChildAnswerBinding
import com.pp.ui.utils.StateView
import com.pp.ui.utils.attachStateView
import com.pp.ui.utils.setPagingAdapter

class AnswerFragment : ThemeFragment<FragmentHomeChildAnswerBinding, AnswerViewModel>() {
    override val mBinding by lazy { FragmentHomeChildAnswerBinding.inflate(layoutInflater) }
    override fun getModelClazz(): Class<AnswerViewModel> {
        return AnswerViewModel::class.java
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStateView()
    }

    private fun initStateView() {
        mBinding.refreshLayout.doOnAttach {

            StateView.DefaultBuilder(mBinding.refreshLayout, mViewModel.mTheme, viewLifecycleOwner)
                .setOnRetry {
                    mViewModel.refresh()
                }
                .build()
                .also {
                    mViewModel.mArticleAdapter.attachStateView(it)
                }
        }

    }
}
package com.pp.home.ui

import androidx.lifecycle.lifecycleScope
import com.pp.base.ThemeFragment
import com.pp.common.paging.articleDifferCallback
import com.pp.home.databinding.FragmentHomeChildSquareBinding
import com.pp.home.model.ArticleItemArticleViewModel
import com.pp.home.model.ChapterItemArticleViewModel
import com.pp.ui.adapter.BindingPagingDataAdapter
import com.pp.ui.databinding.ItemArticleBinding

class SquareFragment : ThemeFragment<FragmentHomeChildSquareBinding, SquareViewModel>() {

    override val mBinding by lazy { FragmentHomeChildSquareBinding.inflate(layoutInflater) }

    override fun getModelClazz(): Class<SquareViewModel> {
        return SquareViewModel::class.java
    }

    override fun onFirstResume() {
        super.onFirstResume()

        val adapter =
            BindingPagingDataAdapter.DefaultBindingPagingDataAdapter(
                onCreateViewDataBinding = { ItemArticleBinding.inflate(layoutInflater, it, false) },
                onCreateItemViewModel = { binding, item ->
                    val viewModel = binding.viewModel
                    if (viewModel is ChapterItemArticleViewModel) {
                        viewModel.also { it.updateArticle(item) }
                    } else {
                        ChapterItemArticleViewModel(item, mViewModel.mTheme)
                    }
                },
                diffCallback = articleDifferCallback
            )

        mBinding.pageListView.setPageAdapter(
            viewLifecycleOwner,
            lifecycleScope,
            mViewModel.getPageData(),
            adapter
        )


    }
}
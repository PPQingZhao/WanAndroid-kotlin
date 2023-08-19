package com.pp.home.ui

import com.pp.common.paging.articleDifferCallback
import com.pp.home.model.ChapterItemArticleViewModel
import com.pp.ui.adapter.BindingPagingDataAdapter
import com.pp.ui.databinding.ItemArticleBinding

class AnswerFragment :ArticleListFragment<AnswerViewModel>() {

    override fun getModelClazz(): Class<AnswerViewModel> {
        return AnswerViewModel::class.java
    }


    override fun onFirstResume() {
        super.onFirstResume()

        setAdapter(
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
        )
    }
}
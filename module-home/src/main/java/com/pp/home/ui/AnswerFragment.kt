package com.pp.home.ui

import androidx.lifecycle.lifecycleScope
import com.pp.base.ThemeFragment
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.paging.articleDifferCallback
import com.pp.home.databinding.FragmentHomeChildAnswerBinding
import com.pp.common.model.ChapterItemArticleViewModel
import com.pp.ui.adapter.BindingPagingDataAdapter
import com.pp.ui.databinding.ItemArticleBinding

class AnswerFragment : ThemeFragment<FragmentHomeChildAnswerBinding, AnswerViewModel>() {
    override val mBinding by lazy { FragmentHomeChildAnswerBinding.inflate(layoutInflater) }
    override fun getModelClazz(): Class<AnswerViewModel> {
        return AnswerViewModel::class.java
    }

    private fun initPagingList() {
        val adapter =
            BindingPagingDataAdapter.DefaultBindingPagingDataAdapter<ItemArticleBinding,ChapterItemArticleViewModel,ArticleBean>(
                onCreateViewDataBinding = { ItemArticleBinding.inflate(layoutInflater, it, false) },
                onBindItemViewModel = { _, item, pos, cachedItemModel ->
                    if (cachedItemModel is ChapterItemArticleViewModel) {
                        cachedItemModel.also { it.updateArticle(item) }
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

    override fun onFirstResume() {
        super.onFirstResume()
        initPagingList()
    }
}
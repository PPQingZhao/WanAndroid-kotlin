package com.pp.home.ui

import androidx.lifecycle.lifecycleScope
import com.pp.base.ThemeFragment
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.model.ChapterItemArticleViewModel
import com.pp.common.paging.articleDifferCallback
import com.pp.home.databinding.FragmentHomeChildSquareBinding
import com.pp.ui.adapter.BindingPagingDataAdapter
import com.pp.ui.databinding.ItemArticleBinding

class SquareFragment : ThemeFragment<FragmentHomeChildSquareBinding, SquareViewModel>() {

    override val mBinding by lazy { FragmentHomeChildSquareBinding.inflate(layoutInflater) }

    override fun getModelClazz(): Class<SquareViewModel> {
        return SquareViewModel::class.java
    }

    private fun initPagingList() {

        val adapter =
            BindingPagingDataAdapter.DefaultBindingPagingDataAdapter<ItemArticleBinding, ChapterItemArticleViewModel, ArticleBean>(
                onCreateViewDataBinding = { ItemArticleBinding.inflate(layoutInflater, it, false) },
                onBindItemViewModel = { _, item, _, cachedItemModel ->
                    if (cachedItemModel is ChapterItemArticleViewModel) {
                        cachedItemModel.apply { updateArticle(item) }
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
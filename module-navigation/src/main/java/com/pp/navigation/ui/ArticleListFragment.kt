package com.pp.navigation.ui

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pp.base.ThemeFragment
import com.pp.common.paging.articleDifferCallback
import com.pp.common.paging.itemWXArticleChapterBindItemType
import com.pp.navigation.databinding.FragmentArticleListBinding
import com.pp.ui.adapter.BindingPagingDataAdapter
import com.pp.ui.utils.setPagingAdapter
import kotlinx.coroutines.launch

class ArticleListFragment private constructor() :
    ThemeFragment<FragmentArticleListBinding, ArticleListViewModel>() {

    companion object {
        const val CID = "cid"

        fun newInstance(cid: Int): ArticleListFragment {
            return ArticleListFragment().apply {
                arguments = Bundle().also { it.putInt(CID, cid) }
            }
        }
    }

    override val mBinding: FragmentArticleListBinding by lazy {
        FragmentArticleListBinding.inflate(
            layoutInflater
        )
    }

    override fun getModelClazz(): Class<ArticleListViewModel> {
        return ArticleListViewModel::class.java
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val cid = it.getInt(CID)
            mViewModel.setCid(cid)
        }
    }

    private fun initPagingList() {
        val adapter = BindingPagingDataAdapter.RecyclerViewBindingAdapterImpl(
            bindingItemType = itemWXArticleChapterBindItemType(
                inflater = layoutInflater,
                theme = mViewModel.mTheme
            ),
            diffCallback = articleDifferCallback
        )

        mBinding.pageList.layoutManager = LinearLayoutManager(requireContext())
        mBinding.pageList.setPagingAdapter(
            viewLifecycleOwner,
            lifecycleScope,
            mViewModel.getSystemArticle(),
            adapter
        )
    }

    override fun onFirstResume() {
        super.onFirstResume()

        lifecycleScope.launch {
            initPagingList()
        }
    }
}
package com.pp.project.ui

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.pp.base.ThemeFragment
import com.pp.common.model.ChapterItemArticleViewModel
import com.pp.common.paging.articleDifferCallback
import com.pp.project.databinding.FragmentCidprojectBinding
import com.pp.ui.adapter.BindingPagingDataAdapter
import com.pp.ui.databinding.ItemProjectarticleBinding
import kotlinx.coroutines.launch

class CidProjectFragment private constructor() :
    ThemeFragment<FragmentCidprojectBinding, CidProjectViewModel>() {

    companion object {
        const val PROJECT_CID = "cid"

        fun newInstance(cid: Int): CidProjectFragment {
            return CidProjectFragment().apply {
                arguments = Bundle().also { it.putInt(PROJECT_CID, cid) }
            }
        }
    }

    override val mBinding: FragmentCidprojectBinding by lazy {
        FragmentCidprojectBinding.inflate(
            layoutInflater
        )
    }

    override fun getModelClazz(): Class<CidProjectViewModel> {
        return CidProjectViewModel::class.java
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val cid = it.getInt(PROJECT_CID)
            mViewModel.setCid(cid)
        }
    }

    private suspend fun initPagingList() {
        val adapter =
            BindingPagingDataAdapter.DefaultBindingPagingDataAdapter(
                onCreateViewDataBinding = {
                    ItemProjectarticleBinding.inflate(layoutInflater, it, false)
                },
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

        mBinding.pageList.setPageAdapter(
            viewLifecycleOwner,
            lifecycleScope,
            mViewModel.getPageData(),
            adapter
        )
    }

    override fun onFirstResume() {
        super.onFirstResume()

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                initPagingList()
            }
        }
    }
}
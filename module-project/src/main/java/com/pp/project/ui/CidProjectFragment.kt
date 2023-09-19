package com.pp.project.ui

import android.os.Bundle
import androidx.core.view.doOnAttach
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pp.base.ThemeFragment
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.paging.articleDifferCallback
import com.pp.common.paging.collectedListener
import com.pp.common.paging.itemProjectArticleBinder
import com.pp.project.databinding.FragmentCidprojectBinding
import com.pp.ui.R
import com.pp.ui.adapter.BindingPagingDataAdapter
import com.pp.ui.utils.StateView
import com.pp.ui.utils.attachStateView
import com.pp.ui.utils.setPagingAdapter
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
        viewLifecycleOwner.lifecycleScope
        val adapter = BindingPagingDataAdapter<ArticleBean>(
            { R.layout.item_projectarticle },
            diffCallback = articleDifferCallback
        ).apply {
            itemProjectArticleBinder(mViewModel.mTheme, mViewModel.viewModelScope).also {
                addItemViewModelBinder(it)
            }

            collectedListener(viewLifecycleOwner.lifecycleScope)
        }


        StateView.DefaultBuilder(mBinding.refreshLayout, mViewModel.mTheme, viewLifecycleOwner)
            .setOnRetry {
                adapter.refresh()
            }
            .build()
            .also {
                adapter.attachStateView(it)
            }

        mBinding.pageList.setPagingAdapter(
            lifecycleScope,
            mViewModel.getPageData(),
            adapter,
            layoutManager = LinearLayoutManager(requireContext()),
            refreshLayout = mBinding.refreshLayout
        )
    }

    override fun onFirstResume() {
        super.onFirstResume()

        lifecycleScope.launch {
            initPagingList()
        }
    }
}
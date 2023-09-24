package com.pp.navigation.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnAttach
import com.pp.base.ThemeFragment
import com.pp.navigation.databinding.FragmentArticleListBinding
import com.pp.ui.utils.StateView
import com.pp.ui.utils.attachStateView

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
                    mViewModel.mWXArticleAdapter.attachStateView(it)
                }
        }

    }

}
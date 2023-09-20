package com.pp.navigation.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnAttach
import androidx.lifecycle.lifecycleScope
import com.pp.base.ThemeFragment
import com.pp.navigation.databinding.FragmentWxarticleBinding
import com.pp.ui.utils.StateView
import com.pp.ui.utils.attachStateView
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WXArticleFragment private constructor() :
    ThemeFragment<FragmentWxarticleBinding, WXArticleViewModel>() {
    override val mBinding: FragmentWxarticleBinding by lazy {
        FragmentWxarticleBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<WXArticleViewModel> {
        return WXArticleViewModel::class.java
    }

    companion object {
        fun newInstance(): WXArticleFragment {
            return WXArticleFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStateView()
    }

    private fun initStateView() {

        mBinding.contentParent.doOnAttach {

            StateView.DefaultBuilder(
                mBinding.refreshLayout,
                mViewModel.mTheme,
                viewLifecycleOwner
            )
                .setOnRetry {
                    mViewModel.pagingDataAdapter.refresh()
                }
                .build()
                .let {
                    mViewModel.pagingDataAdapter.attachStateView(it)
                }

            val stateView = StateView.DefaultBuilder(
                mBinding.contentParent,
                mViewModel.mTheme,
                viewLifecycleOwner
            ).setOnRetry {
                mViewModel.getWXArticleList()
            }
                .build()
                .also {
                    it.showLoading()
                }

            viewLifecycleOwner.lifecycleScope.launch {
                mViewModel.wxArticleList.collectLatest {
                    cancel()
                    if (it.isEmpty()) {
                        stateView.showEmpty()
                    } else {
                        stateView.showContent()
                    }
                }
            }
        }

    }


}

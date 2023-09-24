package com.pp.navigation.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnAttach
import androidx.lifecycle.lifecycleScope
import com.pp.base.ThemeFragment
import com.pp.common.repository.UserRepository
import com.pp.common.repository.getPreferenceUserWhenResume
import com.pp.common.util.showResponse
import com.pp.navigation.databinding.FragmentWxarticleBinding
import com.pp.ui.utils.StateView
import com.pp.ui.utils.attachStateView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        UserRepository.getPreferenceUserWhenResume(viewLifecycleOwner){
            getWXArticleList()
        }
    }

    private val mStateView by lazy {

        StateView.DefaultBuilder(
            mBinding.contentParent,
            mViewModel.mTheme,
            viewLifecycleOwner
        ).setOnRetry {
            getWXArticleList()
        }.build()

    }

    private fun getWXArticleList() {
        mStateView.showLoading()
        viewLifecycleOwner.lifecycleScope.launch {
            val response = withContext(Dispatchers.IO) {
                mViewModel.getWXArticleList()
            }
            mStateView.showResponse(response)

        }
    }

    override fun onFirstResume() {
        getWXArticleList()
    }

}

package com.pp.home.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnAttach
import com.pp.base.ThemeFragment
import com.pp.home.databinding.FragmentHomeChildSquareBinding
import com.pp.ui.utils.StateView
import com.pp.ui.utils.attachStateView

class SquareFragment : ThemeFragment<FragmentHomeChildSquareBinding, SquareViewModel>() {

    override val mBinding by lazy { FragmentHomeChildSquareBinding.inflate(layoutInflater) }

    override fun getModelClazz(): Class<SquareViewModel> {
        return SquareViewModel::class.java
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
                    mViewModel.mArticleAdapter.attachStateView(it)
                }
        }

    }


}
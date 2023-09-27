package com.pp.user.ui

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.transition.MaterialSharedAxis
import com.pp.base.ThemeFragment
import com.pp.common.util.materialSharedAxis
import com.pp.router_service.RouterPath
import com.pp.ui.utils.StateView
import com.pp.ui.utils.attachStateView
import com.pp.user.databinding.FragmentCoinBinding

@Route(path = RouterPath.User.fragment_coin)
class CoinFragment : ThemeFragment<FragmentCoinBinding, CoinViewModel>() {
    override val mBinding by lazy {
        FragmentCoinBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<CoinViewModel> {
        return CoinViewModel::class.java
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = materialSharedAxis(MaterialSharedAxis.X, true)
        exitTransition = materialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStateView()
    }

    private fun initStateView() {

        StateView.DefaultBuilder(
            mBinding.refreshLayout, mViewModel.mTheme, viewLifecycleOwner
        )
            .setOnRetry {
                mViewModel.getCoinInfo()
                mViewModel.mAdapter.refresh()
            }
            .build()
            .also {
                mViewModel.mAdapter.attachStateView(it)
            }

    }

}
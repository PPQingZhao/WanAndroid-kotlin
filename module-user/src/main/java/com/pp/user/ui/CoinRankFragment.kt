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
import com.pp.user.databinding.FragmentCoinRankBinding

@Route(path = RouterPath.User.fragment_coin_range)
class CoinRankFragment : ThemeFragment<FragmentCoinRankBinding, CoinRankViewModel>() {
    override val mBinding by lazy {
        FragmentCoinRankBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<CoinRankViewModel> {
        return CoinRankViewModel::class.java
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = materialSharedAxis(MaterialSharedAxis.Z, true)
        exitTransition = materialSharedAxis(MaterialSharedAxis.Z, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStateView()
    }

    private fun initStateView() {

        StateView.DefaultBuilder(
            mBinding.refreshLayout, mViewModel.mTheme, viewLifecycleOwner
        )
            .setOnRetry { mViewModel.mAdapter.refresh() }
            .build()
            .also {
                mViewModel.mAdapter.attachStateView(it)
            }

    }

}
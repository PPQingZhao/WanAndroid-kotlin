package com.pp.local.ui

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.transition.MaterialSharedAxis
import com.pp.base.ThemeFragment
import com.pp.common.util.materialSharedAxis
import com.pp.local.databinding.FragmentCollectedBinding
import com.pp.router_service.RouterPath
import com.pp.ui.utils.StateView
import com.pp.ui.utils.attachStateView

@Route(path = RouterPath.Local.fragment_collected)
class CollectedFragment : ThemeFragment<FragmentCollectedBinding, CollectedViewModel>() {
    override val mBinding by lazy {
        FragmentCollectedBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<CollectedViewModel> {
        return CollectedViewModel::class.java
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
            .setOnRetry { mViewModel.mAdapter.refresh() }
            .build()
            .also {
                mViewModel.mAdapter.attachStateView(it)
            }

    }

}
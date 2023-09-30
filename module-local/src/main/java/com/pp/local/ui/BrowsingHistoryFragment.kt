package com.pp.local.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnAttach
import com.alibaba.android.arouter.facade.annotation.Route
import com.pp.base.ThemeFragment
import com.pp.local.databinding.FragmentBrowsingHistoryBinding
import com.pp.router_service.RouterPath
import com.pp.ui.utils.StateView
import com.pp.ui.utils.attachStateView

@Route(path = RouterPath.Local.fragment_browsing_history)
class BrowsingHistoryFragment :
    ThemeFragment<FragmentBrowsingHistoryBinding, BrowsingHistoryViewModel>() {
    override val mBinding by lazy {
        FragmentBrowsingHistoryBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<BrowsingHistoryViewModel> {
        return BrowsingHistoryViewModel::class.java
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.recyclerview.doOnAttach {
            val stateView = StateView.DefaultBuilder(
                mBinding.recyclerview,
                mViewModel.mTheme,
                viewLifecycleOwner
            ).build()
            mViewModel.mAdapter.attachStateView(stateView)
        }
    }
}
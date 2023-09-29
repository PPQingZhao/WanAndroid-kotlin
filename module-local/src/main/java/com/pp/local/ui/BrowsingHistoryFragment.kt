package com.pp.local.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.pp.base.ThemeFragment
import com.pp.local.databinding.FragmentBrowsingHistoryBinding
import com.pp.router_service.RouterPath

@Route(path = RouterPath.Local.fragment_browsing_history)
class BrowsingHistoryFragment :
    ThemeFragment<FragmentBrowsingHistoryBinding, BrowsingHistoryViewModel>() {
    override val mBinding by lazy {
        FragmentBrowsingHistoryBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<BrowsingHistoryViewModel> {
        return BrowsingHistoryViewModel::class.java
    }
}
package com.pp.navigation.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.pp.base.ThemeFragment
import com.pp.navigation.databinding.FragmentNavigationBinding
import com.pp.router_service.RouterPath

@Route(path = RouterPath.Navigation.fragment_navigation)
class NavigationFragment : ThemeFragment<FragmentNavigationBinding, NavigationViewModel>() {
    override val mBinding: FragmentNavigationBinding by lazy {
        FragmentNavigationBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<NavigationViewModel> {
        return NavigationViewModel::class.java
    }
}
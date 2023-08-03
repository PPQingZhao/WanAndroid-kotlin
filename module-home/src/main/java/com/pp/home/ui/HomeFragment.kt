package com.pp.home.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.pp.base.ThemeFragment
import com.pp.home.databinding.FragmentHomeBinding
import com.pp.router_service.RouterPath

@Route(path = RouterPath.Home.fragment_home)
class HomeFragment : ThemeFragment<FragmentHomeBinding, HomeViewModel>() {
    override val mBinding: FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }
}
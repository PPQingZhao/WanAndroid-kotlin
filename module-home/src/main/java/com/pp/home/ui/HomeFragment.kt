package com.pp.home.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.pp.base.ThemeFragment
import com.pp.base.helper.TabPagerFragmentHelper
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

    override fun onFirstResume() {
        super.onFirstResume()
        val pagerList = mutableListOf<TabPagerFragmentHelper.TabPager>().apply {
            add(
                TabPagerFragmentHelper.TabPager(
                    { TestFragment("1111111111111111111111") },
                    null,
                    0,
                    0,
                    "1"
                )
            )
            add(
                TabPagerFragmentHelper.TabPager(
                    { TestFragment("2222222222222222222222") },
                    null,
                    0,
                    0,
                    "2"
                )
            )
            add(
                TabPagerFragmentHelper.TabPager(
                    { TestFragment("333333333333333333333") },
                    null,
                    0,
                    0,
                    "3"
                )
            )
        }

        mBinding.homeViewpager2.offscreenPageLimit = 3
        TabPagerFragmentHelper(this, viewLifecycleOwner.lifecycle)
            .addPagers(pagerList)
            .attach(mBinding.homeTabLayout, mBinding.homeViewpager2, true)
    }
}
package com.pp.home.ui

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.pp.base.ThemeFragment
import com.pp.base.helper.TabPagerFragmentHelper
import com.pp.home.R
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pagerList = mutableListOf<TabPagerFragmentHelper.TabPager>().apply {
            add(
                TabPagerFragmentHelper.TabPager(
                    { RealHomeFragment() },
                    null,
                    0,
                    R.string.home,
                    ""
                )
            )
            add(
                TabPagerFragmentHelper.TabPager(
                    { SquareFragment() },
                    null,
                    0,
                    R.string.square,
                    ""
                )
            )
            add(
                TabPagerFragmentHelper.TabPager(
                    { AnswerFragment() },
                    null,
                    0,
                    R.string.answer,
                    ""
                )
            )
        }

        mBinding.homeViewpager2.offscreenPageLimit = 3
        TabPagerFragmentHelper(this, viewLifecycleOwner.lifecycle)
            .addPagers(pagerList)
            .attach(mBinding.homeTabLayout, mBinding.homeViewpager2, true)
    }
}
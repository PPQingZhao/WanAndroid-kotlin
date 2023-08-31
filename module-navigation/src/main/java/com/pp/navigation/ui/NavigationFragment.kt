package com.pp.navigation.ui

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.pp.base.ThemeFragment
import com.pp.base.helper.TabPagerFragmentHelper
import com.pp.navigation.R
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.viewpager2.offscreenPageLimit = 3
        TabPagerFragmentHelper(childFragmentManager, viewLifecycleOwner.lifecycle)
            .addPagers(getPagers())
            .attach(mBinding.tablayout, mBinding.viewpager2)
    }

    private fun getPagers(): List<TabPagerFragmentHelper.TabPager> {
        return mutableListOf<TabPagerFragmentHelper.TabPager>().also {
            it.add(
                TabPagerFragmentHelper.TabPager(
                    { NavigationRealFragment.newInstance() }, text = R.string.navigation
                )
            )

            it.add(
                TabPagerFragmentHelper.TabPager(
                    { SystemFragment.newInstance() }, text = R.string.system
                )
            )

            it.add(
                TabPagerFragmentHelper.TabPager(
                    { WXArticleFragment.newInstance() }, text = R.string.wx_article
                )
            )
        }
    }
}
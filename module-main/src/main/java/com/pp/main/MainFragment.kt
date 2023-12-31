package com.pp.main

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.pp.base.ThemeFragment
import com.pp.base.helper.TabPagerFragmentHelper
import com.pp.main.databinding.FragmentMainBinding
import com.pp.main.widget.WanAndroidTabImageSwitcher
import com.pp.router_service.RouterPath

@Route(path = RouterPath.Main.fragment_main)
class MainFragment : ThemeFragment<FragmentMainBinding, MainFragmentViewModel>() {
    override val mBinding: FragmentMainBinding by lazy {
        FragmentMainBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<MainFragmentViewModel> {
        return MainFragmentViewModel::class.java
    }

    override fun onFirstResume() {
        super.onFirstResume()
        mBinding.mainViewpager2.run {
            val pagerFragments = pagerFragments()
            isUserInputEnabled = false
            offscreenPageLimit = pagerFragments.size
            TabPagerFragmentHelper(childFragmentManager, viewLifecycleOwner.lifecycle)
                .addPagers(pagerFragments)
                .attach(mBinding.mainTabLayout, this, false)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun pagerFragments(): List<TabPagerFragmentHelper.TabPager> {

        return mutableListOf<TabPagerFragmentHelper.TabPager>().apply {
            val homeTagPager = createTagPager(
                RouterPath.Home.fragment_home,
                "ic_tab_selected_home",
                "ic_tab_unselected_home"
            )

            val projectTagPager = createTagPager(
                RouterPath.Project.fragment_project,
                "ic_tab_selected_project",
                "ic_tab_unselected_project"
            )

            val navigationTagPager = createTagPager(
                RouterPath.Navigation.fragment_navigation,
                "ic_tab_selected_navigation",
                "ic_tab_unselected_navigation"
            )

            val userTagPager = createTagPager(
                RouterPath.User.fragment_user,
                "ic_tab_selected_mine",
                "ic_tab_unselected_mine"
            )

            add(homeTagPager)
            add(projectTagPager)
            add(navigationTagPager)
            add(userTagPager)
        }
    }

    private fun createTagPager(
        fragment: String, selectedIconName: String, unSelectedIconName: String,
    ): TabPagerFragmentHelper.TabPager {
        return TabPagerFragmentHelper.TabPager(
            {
                ARouter.getInstance().build(fragment)
                    .navigation() as Fragment
            },
            WanAndroidTabImageSwitcher(
                requireContext(),
                mViewModel.mTheme,
                selectedIconName,
                unSelectedIconName
            )
        )
    }
}
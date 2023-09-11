package com.pp.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.google.android.material.transition.MaterialSharedAxis
import com.pp.base.ThemeFragment
import com.pp.base.helper.TabPagerFragmentHelper
import com.pp.common.app.App
import com.pp.common.util.ViewTreeMultiRouterFragmentViewModel
import com.pp.common.util.materialSharedAxis
import com.pp.main.databinding.FragmentMainBinding
import com.pp.main.widget.WanAndroidTabImageSwitcher
import com.pp.router_service.RouterPath

@Route(path = RouterPath.Main.fragment_main)
class MainFragment : ThemeFragment<FragmentMainBinding, MainViewModel>() {
    override val mBinding: FragmentMainBinding by lazy {
        FragmentMainBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.getInstance().navigation.observe(this) { it ->
            when (it.first) {
                RouterPath.User.fragment_user -> {
                    mBinding.mainViewpager2.setCurrentItem(3, false)
                }
            }
        }
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
            add(
                createTagPager(
                    RouterPath.Home.fragment_home, "ic_tab_selected_home",
                    "ic_tab_unselected_home"
                )
            )

            add(
                createTagPager(
                    RouterPath.Project.fragment_project, "ic_tab_selected_project",
                    "ic_tab_unselected_project"
                )
            )

            add(
                createTagPager(
                    RouterPath.Navigation.fragment_navigation, "ic_tab_selected_navigation",
                    "ic_tab_unselected_navigation"
                )
            )

            add(
                createTagPager(
                    RouterPath.User.fragment_user, "ic_tab_selected_mine",
                    "ic_tab_unselected_mine"
                )
            )
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
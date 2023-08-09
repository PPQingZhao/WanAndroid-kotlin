package com.pp.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.pp.base.ThemeFragment
import com.pp.base.helper.TabPagerFragmentHelper
import com.pp.common.app.App
import com.pp.main.databinding.FragmentMainBinding
import com.pp.main.widget.WanAndroidTabImageSwitcher
import com.pp.router_service.RouterPath

class MainFragment : ThemeFragment<FragmentMainBinding, MainViewModel>() {
    override val mBinding: FragmentMainBinding by lazy {
        FragmentMainBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.getInstance().navigation.observe(this) { t ->
            when (t) {
                RouterPath.User.fragment_user -> {
                    mBinding.mainViewpager2.currentItem = 3
                }
            }
        }
    }

    override fun onFirstResume() {
        super.onFirstResume()
        mBinding.mainViewpager2.run {
            val pagerFragments = pagerFragments()
            isUserInputEnabled = false
            isSaveEnabled = false
            offscreenPageLimit = pagerFragments.size
            TabPagerFragmentHelper(this@MainFragment, viewLifecycleOwner.lifecycle)
                .addPagers(pagerFragments)
                .attach(mBinding.mainTabLayout, this, false)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun pagerFragments(): List<TabPagerFragmentHelper.TabPager> {

        return mutableListOf<TabPagerFragmentHelper.TabPager>().apply {
            add(
                TabPagerFragmentHelper.TabPager(
                    {
                        ARouter.getInstance().build(RouterPath.Home.fragment_home)
                            .navigation() as Fragment
                    },
                    WanAndroidTabImageSwitcher(
                        requireContext(),
                        mViewModel.mTheme,
                        "ic_tab_selected_home",
                        "ic_tab_unselected_home"
                    )
                )
            )

            add(
                TabPagerFragmentHelper.TabPager(
                    {
                        ARouter.getInstance().build(RouterPath.Project.fragment_project)
                            .navigation() as Fragment
                    },
                    WanAndroidTabImageSwitcher(
                        requireContext(),
                        mViewModel.mTheme,
                        "ic_tab_selected_project",
                        "ic_tab_unselected_project"
                    )
                )
            )

            add(
                TabPagerFragmentHelper.TabPager(
                    {
                        ARouter.getInstance().build(RouterPath.Navigation.fragment_navigation)
                            .navigation() as Fragment
                    },
                    WanAndroidTabImageSwitcher(
                        requireContext(),
                        mViewModel.mTheme,
                        "ic_tab_selected_navigation",
                        "ic_tab_unselected_navigation"
                    )
                )
            )

            add(
                TabPagerFragmentHelper.TabPager(
                    {
                        ARouter.getInstance().build(RouterPath.User.fragment_user)
                            .navigation() as Fragment
                    },
                    WanAndroidTabImageSwitcher(
                        requireContext(),
                        mViewModel.mTheme,
                        "ic_tab_selected_mine",
                        "ic_tab_unselected_mine"
                    ).apply {
                        setOnTouchListener { v, event ->
                            App.getInstance().navigation.value = RouterPath.User.fragment_login
                            return@setOnTouchListener true
                        }
                    }
                )
            )
        }
    }

}
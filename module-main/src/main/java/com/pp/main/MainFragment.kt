package com.pp.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.pp.base.ThemeFragment
import com.pp.base.helper.PagerFragmentHelper
import com.pp.main.databinding.FragmentMainBinding
import com.pp.router_service.RouterPath

class MainFragment : ThemeFragment<FragmentMainBinding, MainViewModel>() {

    override val mBinding: FragmentMainBinding by lazy {
        FragmentMainBinding.inflate(
            layoutInflater
        )
    }

    override fun getModelClazz(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PagerFragmentHelper(this)
            .addPagers(pagerFragments())
            .attach(mBinding.mainViewpager2)
    }

    private fun pagerFragments(): MutableList<PagerFragmentHelper.Pager> {
        /* ARouter.getInstance().build(RouterPath.Home.fragment_home)
             .navigation() as Fragment

         ARouter.getInstance().build(RouterPath.Project.fragment_project)
             .navigation() as Fragment

         ARouter.getInstance().build(RouterPath.Navigation.fragment_navigation)
             .navigation() as Fragment*/

        return mutableListOf<PagerFragmentHelper.Pager>().apply {

            add(
                PagerFragmentHelper.Pager(
                    ARouter.getInstance().build(RouterPath.User.fragment_user)
                        .navigation() as Fragment
                )
            )
            add(
                PagerFragmentHelper.Pager(
                    ARouter.getInstance().build(RouterPath.User.fragment_user)
                        .navigation() as Fragment
                )
            )
            add(
                PagerFragmentHelper.Pager(
                    ARouter.getInstance().build(RouterPath.User.fragment_user)
                        .navigation() as Fragment
                )
            )
            add(
                PagerFragmentHelper.Pager(
                    ARouter.getInstance().build(RouterPath.User.fragment_user)
                        .navigation() as Fragment
                )
            )

        }

    }

}
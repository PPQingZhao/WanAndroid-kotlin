package com.pp.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.pp.base.ThemeActivity
import com.pp.base.ThemeViewModel
import com.pp.base.helper.PagerFragmentHelper
import com.pp.base.helper.TabPagerFragmentHelper
import com.pp.main.databinding.ActivityMainBinding
import com.pp.router_service.RouterPath

class MainActivity : ThemeActivity<ActivityMainBinding, ThemeViewModel>() {

    override val mBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<ThemeViewModel> {
        return ThemeViewModel::class.java
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TabPagerFragmentHelper(this)
            .addPagers(pagerFragments())
            .attach(mBinding.flTabLayout, mBinding.mainViewpager2)
    }


    private fun pagerFragments(): MutableList<TabPagerFragmentHelper.TabPager> {

        return mutableListOf<TabPagerFragmentHelper.TabPager>().apply {
            add(
                TabPagerFragmentHelper.TabPager(
                    ARouter.getInstance().build(RouterPath.Home.fragment_home)
                        .navigation() as Fragment,
                    ImageView(baseContext),
                )
            )

            add(
                TabPagerFragmentHelper.TabPager(
                    ARouter.getInstance().build(RouterPath.Project.fragment_project)
                        .navigation() as Fragment,
                    ImageView(baseContext),
                )
            )

            add(
                TabPagerFragmentHelper.TabPager(
                    ARouter.getInstance().build(RouterPath.Navigation.fragment_navigation)
                        .navigation() as Fragment,
                    ImageView(baseContext),
                )
            )

            add(
                TabPagerFragmentHelper.TabPager(
                    ARouter.getInstance().build(RouterPath.User.fragment_user)
                        .navigation() as Fragment,
                    ImageView(baseContext),
                )
            )

        }

    }


}
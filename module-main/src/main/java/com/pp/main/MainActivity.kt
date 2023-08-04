package com.pp.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.launcher.ARouter
import com.pp.base.ThemeActivity
import com.pp.base.WanAndroidTheme
import com.pp.base.getPreferenceTheme
import com.pp.base.helper.TabPagerFragmentHelper
import com.pp.base.updateTheme
import com.pp.main.databinding.ActivityMainBinding
import com.pp.main.databinding.ActivityMainBindingImpl
import com.pp.router_service.RouterPath
import com.pp.ui.widget.TabImageSwitcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ThemeActivity<ActivityMainBinding, MainViewModel>() {

    override val mBinding: ActivityMainBinding by lazy {
        ActivityMainBindingImpl.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<MainViewModel> {
        return MainViewModel::class.java
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
            .attach(mBinding.mainTabLayout, mBinding.mainViewpager2)

        var themeId = WanAndroidTheme.Default
        lifecycleScope.launch(Dispatchers.IO) {
            getPreferenceTheme().collectLatest {
                themeId = it ?: WanAndroidTheme.Default
            }
        }

        mBinding.tvTheme.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                if (themeId == WanAndroidTheme.Default) {
                    updateTheme(WanAndroidTheme.Black)
                } else {
                    updateTheme(WanAndroidTheme.Default)
                }
            }
        }

    }

    private fun pagerFragments(): MutableList<TabPagerFragmentHelper.TabPager> {

        return mutableListOf<TabPagerFragmentHelper.TabPager>().apply {
            add(
                TabPagerFragmentHelper.TabPager(
                    ARouter.getInstance().build(RouterPath.Home.fragment_home)
                        .navigation() as Fragment,
                    TabImageSwitcher(
                        this@MainActivity, com.pp.skin.R.drawable.ic_tab_selected_home_bg,
                        com.pp.skin.R.drawable.ic_tab_selected_home_bg
                    )
                )
            )

            add(
                TabPagerFragmentHelper.TabPager(
                    ARouter.getInstance().build(RouterPath.Project.fragment_project)
                        .navigation() as Fragment,
                    TabImageSwitcher(
                        this@MainActivity, com.pp.skin.R.drawable.ic_tab_selected_home_bg,
                        com.pp.skin.R.drawable.ic_tab_selected_home_bg
                    )
                )
            )

            add(
                TabPagerFragmentHelper.TabPager(
                    ARouter.getInstance().build(RouterPath.Navigation.fragment_navigation)
                        .navigation() as Fragment,
                    TabImageSwitcher(
                        this@MainActivity, com.pp.skin.R.drawable.ic_tab_selected_home_bg,
                        com.pp.skin.R.drawable.ic_tab_selected_home_bg
                    )
                )
            )

            add(
                TabPagerFragmentHelper.TabPager(
                    ARouter.getInstance().build(RouterPath.User.fragment_user)
                        .navigation() as Fragment,
                    TabImageSwitcher(
                        this@MainActivity, com.pp.skin.R.drawable.ic_tab_selected_home_bg,
                        com.pp.skin.R.drawable.ic_tab_selected_home_bg
                    )
                )
            )
        }
    }

}
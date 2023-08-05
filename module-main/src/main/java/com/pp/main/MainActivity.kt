package com.pp.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewTreeLifecycleOwner
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
import com.pp.theme.DynamicTheme
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
    }

    private fun pagerFragments(): MutableList<TabPagerFragmentHelper.TabPager> {

        return mutableListOf<TabPagerFragmentHelper.TabPager>().apply {
            add(
                TabPagerFragmentHelper.TabPager(
                    ARouter.getInstance().build(RouterPath.Home.fragment_home)
                        .navigation() as Fragment,
                    WanAndroidTabImageSwitcher(
                        this@MainActivity,
                        mViewModel.mTheme,
                        "ic_tab_selected_home_bg",
                        "ic_tab_unselected_home_bg"
                    )
                )
            )

            add(
                TabPagerFragmentHelper.TabPager(
                    ARouter.getInstance().build(RouterPath.Project.fragment_project)
                        .navigation() as Fragment,
                    WanAndroidTabImageSwitcher(
                        this@MainActivity,
                        mViewModel.mTheme,
                        "ic_tab_selected_project_bg",
                        "ic_tab_unselected_project_bg"
                    )
                )
            )

            add(
                TabPagerFragmentHelper.TabPager(
                    ARouter.getInstance().build(RouterPath.Navigation.fragment_navigation)
                        .navigation() as Fragment,
                    WanAndroidTabImageSwitcher(
                        this@MainActivity,
                        mViewModel.mTheme,
                        "ic_tab_selected_navigation_bg",
                        "ic_tab_unselected_navigation_bg"
                    )
                )
            )

            add(
                TabPagerFragmentHelper.TabPager(
                    ARouter.getInstance().build(RouterPath.User.fragment_user)
                        .navigation() as Fragment,
                    WanAndroidTabImageSwitcher(
                        this@MainActivity,
                        mViewModel.mTheme,
                        "ic_tab_selected_mine_bg",
                        "ic_tab_unselected_mine_bg"
                    )
                )
            )
        }
    }

    private class WanAndroidTabImageSwitcher(
        context: Context,
        private val theme: DynamicTheme,
        private val selectedIconName: String,
        private val unSelectedIconName: String,
    ) : TabImageSwitcher(context) {

        override fun onDetachedFromWindow() {
            super.onDetachedFromWindow()
            ViewTreeLifecycleOwner.get(this)?.run {
                theme.themeInfo.removeObserver(themeInfoObserver)
            }
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        private val themeInfoObserver = Observer<DynamicTheme.Info> {
            it?.theme?.resources?.run {
                getIdentifier(selectedIconName, "drawable", it.themePackage).run {
                    Log.e("TAG","selectedIconName: $selectedIconName")
                    getDrawable(this, it.theme).run { mSelectedImageView.setImageDrawable(this) }
                }

                getIdentifier(unSelectedIconName, "drawable", it.themePackage).run {
                    Log.e("TAG","unSelectedIconName: $unSelectedIconName")
                    getDrawable(this, it.theme).run { mUnSelectedImageView.setImageDrawable(this) }
                }
            }
        }

        override fun onAttachedToWindow() {
            super.onAttachedToWindow()
            ViewTreeLifecycleOwner.get(this)?.run {
                theme.themeInfo.observe(this, themeInfoObserver)
            }
        }
    }

}
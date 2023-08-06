package com.pp.base.helper

import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TabPagerFragmentHelper {
    private val pagerHelper: PagerFragmentHelper
    private var tabPagerList = mutableListOf<TabPager>()

    constructor(manager: FragmentManager, lifecycle: Lifecycle) {
        pagerHelper = PagerFragmentHelper(manager, lifecycle)
    }

    constructor(
        activity: FragmentActivity,
    ) : this(activity.supportFragmentManager, activity.lifecycle)

    constructor(
        fragment: Fragment,
    ) : this(fragment.childFragmentManager, fragment.lifecycle)

    fun addPager(pager: TabPager): TabPagerFragmentHelper {
        tabPagerList.add(pager)
        pagerHelper.addPager(pager)
        return this
    }

    fun addPagers(pagers: List<TabPager>): TabPagerFragmentHelper {
        tabPagerList.addAll(pagers)
        pagerHelper.addPagers(pagers)
        return this
    }

    fun attach(tabLayout: TabLayout, viewPager2: ViewPager2, smoothScroll: Boolean = true) {
        pagerHelper.attach(viewPager2)

        //TabLayout联动ViewPager
//        tabLayout.clearOnTabSelectedListeners()
        tabLayout.removeAllTabs()

        TabLayoutMediator(
            tabLayout,
            viewPager2,
            true,
            smoothScroll
        ) { tab, position ->
            val pTab = tabPagerList[position].tab
            tab.customView = pTab.tab
            if (pTab.icon > 0) {
                tab.setIcon(pTab.icon)
            }
            if (pTab.text > 0) {
                tab.setText(pTab.text)
            }
            if (pTab.title.isNotEmpty()) {
                tab.text = pTab.title
            }

        }.attach()
    }

    class TabPager : PagerFragmentHelper.Pager {
        internal val tab: Tab

        constructor(
            fragment: Fragment,
            tabView: View?,
            @DrawableRes icon: Int = 0,
            @StringRes text: Int = 0,
            title: String = "",
        ) : super(fragment) {
            tab = Tab(tabView, icon, text, title)
        }
    }

    internal class Tab(
        val tab: View?,
        @DrawableRes val icon: Int = 0,
        @StringRes val text: Int = 0,
        val title: String = "",
    )
}
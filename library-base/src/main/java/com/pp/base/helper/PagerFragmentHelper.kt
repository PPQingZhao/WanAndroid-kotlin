package com.pp.base.helper

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

/**
 * viewpager fragment
 */
class PagerFragmentHelper {
    private val adapter: FragmentAdapter
    private var pagerList = mutableListOf<Pager>()

    constructor(manager: FragmentManager, lifecycle: Lifecycle) {
        adapter = FragmentAdapter(manager, lifecycle)
    }

    fun addPager(pager: Pager): PagerFragmentHelper {
        pagerList.add(pager)
        return this
    }

    fun addPagers(pagers: List<Pager>): PagerFragmentHelper {
        pagerList.addAll(pagers)
        return this
    }

    fun attach(pager: ViewPager2): PagerFragmentHelper {
        pager.adapter = adapter
        val firstChild = pager.getChildAt(0)
        if (firstChild is RecyclerView) {
            firstChild.overScrollMode = View.OVER_SCROLL_NEVER
        }
        return this
    }

    /**
     * adapter for ViewPager2
     */
    private inner class FragmentAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fm, lifecycle) {
        override fun getItemCount(): Int {
            return pagerList.size
        }

        override fun createFragment(position: Int): Fragment {
            return pagerList[position].getFragment()
        }
    }

    open class Pager(private val f: () -> Fragment) {
        fun getFragment(): Fragment {
            return f.invoke()
        }
    }

}
package com.pp.ui.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener

/**
 * floating button 显示/隐藏
 */
class FloatingScrollerListener(private val floatButton: View) : OnScrollListener() {

    init {
        floatButton.visibility = View.GONE
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (newState != RecyclerView.SCROLL_STATE_IDLE) {
            return
        }
        val canScrollToTop =
            recyclerView.canScrollVertically(-1) || recyclerView.canScrollHorizontally(-1)
        if (!canScrollToTop) {
            animateOut(floatButton)
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val floatDistance = if (dx == 0) dy else dx

        if (floatDistance >= 0) {
            animateOut(floatButton)
        } else {
            animateIn(floatButton)
        }
    }

    private var animateOut: ViewPropertyAnimator? = null
    private var animateIn: ViewPropertyAnimator? = null

    // FAB移出屏幕动画（隐藏动画）
    private fun animateOut(fab: View) {
        animateIn = null
        if (null == animateOut) {
            animateOut = fab.animate().scaleX(0f).scaleY(0f).setDuration(200)
                .setInterpolator(LinearInterpolator())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        floatButton.visibility = View.GONE
                    }
                })
            animateOut?.start()
        }
    }

    // FAB移入屏幕动画（显示动画）
    private fun animateIn(fab: View) {
        animateOut = null
        if (null == animateIn) {
            fab.visibility = View.VISIBLE
            animateIn = fab.animate().scaleX(1f).scaleY(1f).setDuration(200)
                .setInterpolator(LinearInterpolator())
                .setListener(null)
            animateIn?.start()
        }
    }

}
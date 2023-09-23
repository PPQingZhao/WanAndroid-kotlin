package com.pp.ui.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.util.Log
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
        animateOut(floatButton)
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//        Log.e("TAG", "newState: $newState")
        if (newState != RecyclerView.SCROLL_STATE_IDLE) {
            return
        }

        shouldAnimateOut(recyclerView)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val floatDistance = if (dx == 0) dy else dx

//        Log.e("TAG", "distance: $floatDistance  dx: $dx  dy: $dy")
        // floatDistance == 0 时: 在滑动过程中可能会接收多个 dy = 0,dx =0   比如 TabSystemFragment页面(猜测motionLayout嵌套viewpager2再嵌套recyclerview原因)
        if (floatDistance > 0) {
            animateOut(floatButton)
        } else if (floatDistance < 0) {
            animateIn(floatButton)
        } else {
            shouldAnimateOut(recyclerView)
        }
    }

    private fun shouldAnimateOut(recyclerView: RecyclerView) {
        val canScrollToTop =
            recyclerView.canScrollVertically(-1) || recyclerView.canScrollHorizontally(-1)
        if (!canScrollToTop) {
            animateOut(floatButton)
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
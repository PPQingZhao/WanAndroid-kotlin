package com.pp.ui.utils

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FloatingTopActionBehavior : CoordinatorLayout.Behavior<FloatingActionButton> {

    constructor() : super()

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int,
    ): Boolean {
        // 纵向
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }


    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray,
    ) {
        super.onNestedScroll(
            coordinatorLayout,
            child,
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            type,
            consumed
        )

        Log.e("TAG","onNestedScroll")

        if (dyConsumed > 0) { // 向下滑动
            animateOut(child)
        } else if (dyConsumed < 0) { // 向上滑动
            animateIn(child)
        }
    }

    // FAB移出屏幕动画（隐藏动画）
    private fun animateOut(fab: FloatingActionButton) {
        fab.animate().scaleX(0f).scaleY(0f).setDuration(200).setInterpolator(LinearInterpolator())
            .start()
    }

    // FAB移入屏幕动画（显示动画）
    private fun animateIn(fab: FloatingActionButton) {
        fab.animate().scaleX(1f).scaleY(1f).setDuration(200).setInterpolator(LinearInterpolator())
            .start()
        fab.visibility = View.VISIBLE
    }

}
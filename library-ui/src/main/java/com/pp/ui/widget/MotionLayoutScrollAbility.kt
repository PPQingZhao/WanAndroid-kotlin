package com.pp.ui.widget

import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter

class MotionLayoutScrollAbility :
    NestedScrollableHost.ViewScrollAbility {
    override fun canScrollHorizontally(view: View, direction: Int): Boolean {
        if (view is MotionLayout) {
            return view.progress == 0f || view.progress == 1f
        }
        return false
    }

    override fun canScrollVertically(view: View, direction: Int): Boolean {
        if (view is MotionLayout) {
            return view.progress == 0f || view.progress == 1f
        }
        return false
    }
}
package com.pp.ui.widget

import android.view.View
import androidx.constraintlayout.helper.widget.Carousel
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter

class BannerMotionLayoutScrollAbility(
    private val carousel: Carousel,
) :
    NestedScrollableHost.ViewScrollAbility {
    override fun canScrollHorizontally(view: View, direction: Int): Boolean {
        if (view is MotionLayout) {
            if (carousel.isInfinite) {
                return true
            }

            if (1 == carousel.count) {
                return false
            }

            if (0 < carousel.currentIndex && carousel.currentIndex < carousel.count - 1) {
                return true
            }

            if (0 == carousel.currentIndex) {
                return direction > 0
            }

            if (carousel.count - 1 == carousel.currentIndex) {
                return direction < 0
            }
            return false
        }
        return false
    }

    override fun canScrollVertically(view: View, direction: Int): Boolean {
        if (view is MotionLayout) {
            if (carousel.isInfinite) {
                return true
            }

            if (1 == carousel.count) {
                return false
            }

            if (0 < carousel.currentIndex && carousel.currentIndex < carousel.count - 1) {
                return true
            }

            if (0 == carousel.currentIndex) {
                return direction > 0
            }

            if (carousel.count - 1 == carousel.currentIndex) {
                return direction < 0
            }
            return false
        }
        return false
    }
}
package com.pp.ui.widget

import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import com.pp.ui.utils.BannerCarousel

class BannerMotionLayoutScrollAbility(
    private val carousel: BannerCarousel,
) :
    NestedScrollableHost.ViewScrollAbility {
    override fun canScrollHorizontally(view: View, direction: Int): Boolean {
        if (view is MotionLayout) {

            if (1 == carousel.count) {
                return false
            }

            if (carousel.isInfinite) {
                return true
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

            if (1 == carousel.count) {
                return false
            }

            if (carousel.isInfinite) {
                return true
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
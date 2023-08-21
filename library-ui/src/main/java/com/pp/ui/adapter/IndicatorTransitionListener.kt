package com.pp.ui.adapter

import androidx.constraintlayout.motion.widget.MotionLayout
import com.pp.ui.indicator.IndicatorView
import com.pp.ui.utils.BannerCarousel

class IndicatorTransitionListener(private val indicatorView: IndicatorView) :
    BannerCarousel.TransitionListener {
    override fun onTransitionChange(
        motionLayout: MotionLayout?,
        startId: Int,
        endId: Int,
        progress: Float,
        carousel: BannerCarousel?,
        curIndex: Int,
        nextStateId: Int,
        previousStateId: Int,
    ) {

        carousel?.let {
            var position = curIndex
            var positionOffset = progress
            // backward
            if (endId == previousStateId) {
                position = curIndex - 1
                if (position < 0) {
                    position = it.count - 1
                }
                positionOffset = 1 - progress
            }
            indicatorView.setPosition(position, positionOffset)
        }
    }

    override fun onTransitionCompleted(
        motionLayout: MotionLayout?,
        currentId: Int,
        carousel: BannerCarousel?,
        curIndex: Int,
        nextStateId: Int,
        previousStateId: Int,
    ) {
        indicatorView.setPosition(curIndex, 0f)
    }
}
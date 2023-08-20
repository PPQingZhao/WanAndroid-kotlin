package com.pp.ui.adapter

import androidx.constraintlayout.helper.widget.Carousel
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import com.pp.ui.indicator.IndicatorView

class IndicatorTransitionAdapter(
    private val indicatorView: IndicatorView,
    private val carousel: Carousel,
) : TransitionAdapter() {

    override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {

    }

    override fun onTransitionChange(
        motionLayout: MotionLayout?,
        startId: Int,
        endId: Int,
        progress: Float,
    ) {
        motionLayout?.let {
            it.targetPosition

            var position = carousel.currentIndex
            val previousIndex1 = previousIndex
            if (it.currentState == startId) {
                position = carousel.currentIndex - 1
            }
            indicatorView.setPosition(position, progress)
        }
    }

    var previousIndex = -1
    override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
        previousIndex = carousel.currentIndex
    }
}
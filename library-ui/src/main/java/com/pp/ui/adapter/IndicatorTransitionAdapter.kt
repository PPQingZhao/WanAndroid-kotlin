package com.pp.ui.adapter

import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import com.pp.ui.indicator.IndicatorView
import com.pp.ui.utils.BannerCarousel
import kotlin.math.sign

class IndicatorTransitionAdapter(
    private val indicatorView: IndicatorView,
    private val carousel: BannerCarousel,
) : TransitionAdapter() {

    override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {
        motionLayout?.let {
            val targetPosition = it.targetPosition
            val progress1 = it.progress

            val dir = Math.signum(targetPosition - progress1)
//            Log.e("TAG", "start dir: $dir  targetPosition: targetPosition  $targetPosition progress1: $progress1")
        }
    }

    override fun onTransitionChange(
        motionLayout: MotionLayout?,
        startId: Int,
        endId: Int,
        progress: Float,
    ) {
        motionLayout?.let {
            val targetPosition = it.targetPosition
            val progress1 = it.progress


            val dir = sign(targetPosition - progress1)
            val dir2 = Math.signum(targetPosition - progress1)
//            Log.e("TAG", "onTransitionChange dir: $dir dir2: $dir2 targetPosition: targetPosition  $targetPosition progress1: $progress1")

            var position = carousel.currentIndex
            if (it.currentState == startId) {
                position -= 1
            }
            indicatorView.setPosition(position, progress)
        }
    }

}
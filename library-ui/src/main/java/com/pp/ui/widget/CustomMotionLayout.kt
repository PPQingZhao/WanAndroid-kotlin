package com.pp.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.constraintlayout.motion.widget.MotionLayout
import kotlin.math.abs

class CustomMotionLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    MotionLayout(context, attrs, defStyleAttr) {
    private var mTouchSlop: Int = 0

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        val vc = ViewConfiguration.get(context)
        mTouchSlop = vc.scaledTouchSlop
    }

    private var mInitX = 0f
    private var mInitY = 0f
    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        val intercept = super.onInterceptTouchEvent(event)
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mInitX = event.x
                mInitY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                if (!intercept) {
                    val moveX = abs(mInitX - event.x)
                    val moveY = abs(mInitY - event.y)
                    if (moveX > mTouchSlop || moveY > mTouchSlop) {
                        val motionEventDown = MotionEvent.obtain(event).also {
                            it.setLocation(mInitX, mInitY)
                            it.action = MotionEvent.ACTION_DOWN
                        }
                        onTouchEvent(motionEventDown)

                        val motionEventMove = MotionEvent.obtain(event)
                        onTouchEvent(motionEventMove)
                        return true
                    }
                }
            }
        }
        return intercept
    }
}
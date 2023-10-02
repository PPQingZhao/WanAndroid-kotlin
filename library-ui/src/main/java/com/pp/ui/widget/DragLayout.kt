package com.pp.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.customview.widget.ViewDragHelper
import com.pp.ui.R

/**
 * 简单的drag parent
 */
class DragLayout : FrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    @SuppressLint("Recycle")
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    inner class ViewDragCallback : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
//            Log.e("TAG", "tryCaptureView")
            val layoutParams = child.layoutParams
            return (layoutParams as LayoutParam).dragEnable
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return top
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            return left
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {

            val finalLeft = if (releasedChild.left < 0) {
                0
            } else if (releasedChild.right > width) {
                width - releasedChild.width
            } else if ((width - releasedChild.right) < releasedChild.left) {
                width - releasedChild.width
            } else {
                0
            }

            val finalTop = if (releasedChild.bottom > height) {
                height - releasedChild.height
            } else if (releasedChild.top < 0) {
                0
            } else {
                releasedChild.top
            }
            viewDragHelper.settleCapturedViewAt(finalLeft, finalTop)
            postInvalidateOnAnimation()
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return if ((child.layoutParams as LayoutParam).dragEnable) {
                1
            } else {
                0
            }
        }

        override fun getViewHorizontalDragRange(child: View): Int {
            return if ((child.layoutParams as LayoutParam).dragEnable) {
                1
            } else {
                0
            }
        }
    }

    private val viewDragHelper = ViewDragHelper.create(this, ViewDragCallback())

    override fun computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            postInvalidateOnAnimation()
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (null == ev) return super.onInterceptTouchEvent(ev)

        val shouldIntercept = viewDragHelper.shouldInterceptTouchEvent(ev)
//        Log.e("TAG", "onInterceptTouchEvent: ${ev.action} $shouldIntercept")
        return shouldIntercept
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (null == event) return super.onTouchEvent(event)
//        Log.e("TAG", "onTouchEvent: ${event.action}")

        viewDragHelper.processTouchEvent(event)
        return viewDragHelper.viewDragState == ViewDragHelper.STATE_DRAGGING
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return LayoutParam(context, attrs)
    }

    class LayoutParam : FrameLayout.LayoutParams {
        var dragEnable = false

        constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DragLayout_Layout)
            dragEnable = typedArray.getBoolean(R.styleable.DragLayout_Layout_dragEnable, dragEnable)
            typedArray.recycle()
        }

    }
}
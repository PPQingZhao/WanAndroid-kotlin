package com.pp.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2

open class NestedScrollableHost(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    FrameLayout(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private var touchSlop = 0
    private var initialX = 0f
    private var initialY = 0f
    private var mViewScrollAbility: ViewScrollAbility? = null

    init {
        val var10001 = ViewConfiguration.get(context)
        this.touchSlop = var10001.scaledTouchSlop
    }

    fun setChildScrollAbility(ability: ViewScrollAbility?) {
        this.mViewScrollAbility = ability
    }

    private fun getParentViewPager(): ViewPager2? {
        var mViewParent = this.parent
        if (mViewParent !is View) {
            mViewParent = null
        }
        var view: View?
        view = mViewParent as View

        while (view != null && view !is ViewPager2) {
            mViewParent = view.parent
            if (mViewParent !is View) {
                mViewParent = null
            }
            view = if (mViewParent == null) null else mViewParent as View
        }
        val var2: View? = if (view !is ViewPager2) null else view
        return if (var2 == null) null else var2 as ViewPager2
    }

    override fun onViewAdded(child: View?) {
        if (childCount > 1) {
            throw IllegalStateException(
                javaClass.simpleName + " supports only one child"
            )
        }
    }

    private fun getChild(): View? {
        return if (this.childCount > 0) getChildAt(0) else null
    }

    private fun canScrollHorizontally(child: View?, direction: Int): Boolean {
        if (null == child) return false
        if (null != mViewScrollAbility) {
            return mViewScrollAbility!!.canScrollHorizontally(child, direction)
        }
        return child.canScrollHorizontally(direction)
    }

    private fun canScrollVertically(child: View?, direction: Int): Boolean {
        if (null == child) return false
        if (null != mViewScrollAbility) {
            return mViewScrollAbility!!.canScrollVertically(child, direction)
        }
        return child.canScrollVertically(direction)
    }

    private fun canChildScroll(orientation: Int, delta: Float): Boolean {
        val direction = -Math.signum(delta).toInt()
        val child: View?
        var result = false
        when (orientation) {
            ViewPager2.ORIENTATION_HORIZONTAL -> {
                child = getChild()
                result = canScrollHorizontally(child, direction)
            }
            ViewPager2.ORIENTATION_VERTICAL -> {
                child = getChild()
                result = canScrollVertically(child, direction)
            }
            else -> {
            }
        }
        return result
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        this.handleInterceptTouchEvent(ev)
        return super.onInterceptTouchEvent(ev)
    }

    private fun handleInterceptTouchEvent(e: MotionEvent) {
        val viewPager2 = getParentViewPager()
        if (viewPager2 != null) {
            val orientation = viewPager2.orientation
            if (canChildScroll(orientation, -1.0f) || canChildScroll(orientation, 1.0f)) {
                when (e.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = e.x
                        initialY = e.y
                        this.parent.requestDisallowInterceptTouchEvent(true)
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val dx = e.x - initialX
                        val dy = e.y - initialY
                        val isVpHorizontal = orientation == ViewPager2.ORIENTATION_HORIZONTAL
                        val scaledDx = Math.abs(dx) * if (isVpHorizontal) 0.5f else 1.0f
                        val scaledDy = Math.abs(dy) * if (isVpHorizontal) 1.0f else 0.5f
                        if (scaledDx > touchSlop.toFloat() || scaledDy > touchSlop.toFloat()) {
                            when {
//                                isVpHorizontal == scaledDy > scaledDx -> {
//                                    this.parent.requestDisallowInterceptTouchEvent(false)
//                                }
                                canChildScroll(orientation, if (isVpHorizontal) dx else dy) -> {
                                    this.parent.requestDisallowInterceptTouchEvent(true)
                                }
                                else -> {
                                    this.parent.requestDisallowInterceptTouchEvent(false)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    interface ViewScrollAbility {
        fun canScrollHorizontally(view: View, direction: Int): Boolean
        fun canScrollVertically(view: View, direction: Int): Boolean
    }

}
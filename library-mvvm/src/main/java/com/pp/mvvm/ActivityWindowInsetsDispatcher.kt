package com.pp.mvvm

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData

/**
 * 自定义 activity windowInsets分发
 * 分发windowInsets给每一个fragment
 */
class ActivityWindowInsetsDispatcher : DefaultLifecycleObserver {

    private var supportManager: FragmentManager? = null
    private var contentView: View? = null
    private val mWindowInsets = MutableLiveData<WindowInsets>()

    private var fragmentLifecycleCallbacks: FragmentManager.FragmentLifecycleCallbacks? = null

    private fun dispatch(a: FragmentActivity) {
        supportManager = a.supportFragmentManager
        contentView = a.window.decorView.findViewById(android.R.id.content)
        a.lifecycle.addObserver(this)
    }

    companion object {
        fun dispatch(a: FragmentActivity) {
            ActivityWindowInsetsDispatcher().dispatch(a)
        }
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        contentView?.setOnApplyWindowInsetsListener { view, windowInsets ->
            // 记录windowInsets
            mWindowInsets.value = WindowInsets(windowInsets)

            contentView?.apply {
                // 分发windowInsets给 activity布局
                (this as ViewGroup).getChildAt(0)
                    ?.dispatchApplyWindowInsets(WindowInsets(windowInsets))
            }

            // 返回一个已消费的 windowInsets,结束分发流程
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                WindowInsets.CONSUMED
            } else {
                windowInsets.consumeSystemWindowInsets()
            }
        }

        fragmentLifecycleCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentViewCreated(
                fm: FragmentManager,
                f: Fragment,
                v: View,
                savedInstanceState: Bundle?,
            ) {
                Log.e("WindowInsetsDispatcher", "onFragmentViewCreated==>> ${f}")
                // 分发windowInsets 给fragment
                mWindowInsets.observe(f) {
                    it?.apply {
                        f.view?.let { view ->
                            ViewCompat.dispatchApplyWindowInsets(
                                view,
                                WindowInsetsCompat.toWindowInsetsCompat(WindowInsets(it))
                            )
                        }
                    }
                }

            }
        }
        supportManager?.registerFragmentLifecycleCallbacks(fragmentLifecycleCallbacks!!, true)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        contentView = null
        mWindowInsets.value = null

        supportManager?.unregisterFragmentLifecycleCallbacks(fragmentLifecycleCallbacks!!)
        fragmentLifecycleCallbacks = null
        supportManager = null
    }
}
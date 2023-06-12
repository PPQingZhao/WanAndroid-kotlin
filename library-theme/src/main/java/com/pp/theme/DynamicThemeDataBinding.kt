package com.pp.theme

import android.view.Window
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class DynamicThemeDataBinding(
    private var binding: ViewDataBinding?,
    private var window: Window?,
) :
    DefaultLifecycleObserver {

    fun binding(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }

    val mThemeViewModel = DynamicTheme()

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        binding?.apply {
            setVariable(BR.dynamicThemeViewModel, mThemeViewModel)
            ViewTreeAppThemeViewModel[root] = mThemeViewModel
        }

        mThemeViewModel.windowBackground.observe(owner) {
            // 主题窗口背景发生变化时,主动给window设置
            window?.setBackgroundDrawable(it)
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        binding = null
        window = null
    }

}
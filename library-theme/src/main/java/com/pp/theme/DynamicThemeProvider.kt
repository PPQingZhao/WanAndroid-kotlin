package com.pp.theme

import androidx.activity.ComponentActivity
import androidx.databinding.ViewDataBinding

class DynamicThemeProvider {
    private val mThemeViewModel = DynamicTheme()
    private fun create(a: ComponentActivity, binding: ViewDataBinding): DynamicTheme {
        binding.apply {
            setVariable(BR.dynamicThemeViewModel, mThemeViewModel)
            ViewTreeAppThemeViewModel[root] = mThemeViewModel
        }

        mThemeViewModel.windowBackground.observe(a) {
            // 主题窗口背景发生变化时,主动给window设置
            a.window?.setBackgroundDrawable(it)
        }

        return mThemeViewModel
    }

    companion object {
        fun create(a: ComponentActivity, binding: ViewDataBinding): DynamicTheme {
            return DynamicThemeProvider().create(a, binding)
        }
    }

}
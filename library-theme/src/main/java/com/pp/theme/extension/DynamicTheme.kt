package com.pp.theme.extension

import androidx.activity.ComponentActivity
import com.pp.theme.AppDynamicTheme


fun AppDynamicTheme.init(activity: ComponentActivity) {
    windowBackground.observe(activity) {
        // 主题窗口背景发生变化时,主动给window设置
        activity.window?.setBackgroundDrawable(it)
    }
}
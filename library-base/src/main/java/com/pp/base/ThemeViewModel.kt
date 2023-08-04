package com.pp.base

import android.app.Application
import com.pp.mvvm.LifecycleViewModel
import com.pp.theme.AppDynamicTheme

open class ThemeViewModel(app: Application) : LifecycleViewModel(app) {
    val mTheme: AppDynamicTheme = AppDynamicTheme()
}
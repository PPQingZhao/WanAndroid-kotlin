package com.pp.base

import android.app.Application
import android.content.res.Configuration
import android.util.DisplayMetrics
import com.pp.mvvm.LifecycleViewModel
import com.pp.theme.AppDynamicTheme
import com.pp.theme.getDynamicTheme

open class ThemeViewModel(app: Application) : LifecycleViewModel(app) {
    open val mThemeName: String = "Theme.Dynamic"
    open val mTheme: AppDynamicTheme = getDynamicTheme<AppDynamicTheme>(
        getThemeName(),
        DisplayMetrics().apply { setTo(app.theme.resources.displayMetrics) },
        Configuration().apply { setTo(app.theme.resources.configuration) }
    )


    private fun getThemeName(): String {
        return mThemeName
    }

}
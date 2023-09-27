package com.pp.base

import android.app.Application
import android.content.res.Resources
import androidx.lifecycle.viewModelScope
import com.pp.mvvm.LifecycleViewModel
import com.pp.theme.AppDynamicTheme
import com.pp.theme.applySkinTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class ThemeViewModel(app: Application) : LifecycleViewModel(app) {
    val mTheme: AppDynamicTheme = AppDynamicTheme()

    internal fun applySkinTheme(theme: Resources.Theme) {
        viewModelScope.launch(Dispatchers.IO) {
            mTheme.applySkinTheme(theme, getThemeName())
        }
    }

    private fun getThemeName(): String {
        return "Theme.Dynamic"
    }

}
package com.pp.local.model

import android.content.res.Resources.Theme
import android.view.View
import androidx.annotation.StringRes
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import com.pp.local.R
import com.pp.theme.AppDynamicTheme

open class ItemThemeSettingViewModel(
    protected val defaultTheme: Theme,
    val themeId: Int,
    @StringRes private val themName: Int,
) {
    val checked = ObservableBoolean(false)
    var appTheme: AppDynamicTheme = AppDynamicTheme()
    val themeName = ObservableInt(themName)


    open fun onItemClick(v: View) {}
}
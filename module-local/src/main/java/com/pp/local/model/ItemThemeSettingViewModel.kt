package com.pp.local.model

import android.view.View
import androidx.databinding.ObservableBoolean
import com.pp.theme.AppDynamicTheme

open class ItemThemeSettingViewModel(val themeId: Int) {
    val checked = ObservableBoolean(false)
    var appTheme: AppDynamicTheme = AppDynamicTheme()


    open fun onItemClick(v: View) {}
}
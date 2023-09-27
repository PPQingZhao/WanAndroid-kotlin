package com.pp.theme.factory

import android.content.res.Resources
import android.content.res.Resources.Theme
import com.pp.theme.DynamicThemeManager

class DefaultThemeInfoFactory : DynamicThemeManager.ThemeFactory {
    override fun create(
        defaultTheme: Theme,
        skinPath: String,
        themePackage: String,
        themeName: String,
    ): Resources.Theme {
        return defaultTheme
    }


}
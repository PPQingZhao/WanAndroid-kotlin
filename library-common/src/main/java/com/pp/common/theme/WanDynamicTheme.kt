package com.pp.common.theme

import android.content.res.Resources
import androidx.annotation.StringRes
import com.pp.common.constant.getSkin
import com.pp.theme.DynamicThemeManager
import com.pp.theme.factory.DefaultThemeInfoFactory
import com.pp.theme.factory.SkinThemeFactory2
import com.pp.ui.R

sealed class WanDynamicTheme(
    skinPackage: String,
    skinPath: String,
    @StringRes name: Int,
    private val factory: DynamicThemeManager.ThemeFactory,
) :
    DynamicThemeManager.ApplySkinTheme(skinPackage, skinPath, name) {

    override fun create(defaultTheme: Resources.Theme, themeName: String): Resources.Theme {
        return factory.create(
            defaultTheme, themePackage = skinPackage, themeName = themeName, skinPath = skinPath
        )
    }

    object Default :
        WanDynamicTheme("com.pp.wanandroid", "", R.string.theme_default, DefaultThemeInfoFactory())

    object Blue :
        WanDynamicTheme(
            "com.pp.skin", getSkin("skinBlue.skin"), R.string.theme_blue, SkinThemeFactory2()
        )

    object Black :
        WanDynamicTheme(
            "com.pp.skin", getSkin("skinBlack.skin"), R.string.theme_black, SkinThemeFactory2()
        )
}




package com.pp.common.theme

import android.content.res.Configuration
import android.content.res.Resources
import android.util.DisplayMetrics
import androidx.annotation.StringRes
import com.pp.common.app.App
import com.pp.common.constant.getSkin
import com.pp.theme.DynamicThemeManager
import com.pp.theme.factory.DefaultThemeInfoFactory
import com.pp.theme.factory.SkinThemeInfoFactory
import com.pp.ui.R

sealed class WanDynamicTheme(
    skinPackage: String,
    skinPath: String,
    @StringRes name: Int,
    private val factory: DynamicThemeManager.ThemeFactory,
) :
    DynamicThemeManager.ApplySkinTheme(skinPackage, skinPath, name) {

    override fun create(
        displayMetrics: DisplayMetrics,
        configuration: Configuration,
        themeName: String,
    ): Resources.Theme? {
        return factory.create(
            displayMetrics = displayMetrics,
            configuration = configuration,
            themePackage = skinPackage,
            themeName = themeName,
            skinPath = skinPath
        )
    }

    object Default :
        WanDynamicTheme(
            "com.pp.wanandroid",
            "",
            R.string.theme_default,
            DefaultThemeInfoFactory(App.getInstance())
        )

    object Blue :
        WanDynamicTheme(
            "com.pp.skin", getSkin("skinBlue.skin"), R.string.theme_blue, SkinThemeInfoFactory()
        )

    object Black :
        WanDynamicTheme(
            "com.pp.skin", getSkin("skinBlack.skin"), R.string.theme_black, SkinThemeInfoFactory()
        )
}




package com.pp.common.theme

import android.content.res.Resources
import android.os.Environment
import androidx.annotation.StringRes
import com.pp.theme.DynamicThemeManager
import com.pp.theme.factory.DefaultThemeInfoFactory
import com.pp.theme.factory.SkinThemeFactory2
import com.pp.ui.R
import java.io.File

/**
 * 主题包存储路径
 */
var skinPath =
    Environment.getExternalStorageDirectory().absolutePath +
            File.separator + "wanandroid" +
            File.separator + "theme" +
            File.separator + "skin" +
            File.separator

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
            "com.pp.skin", skinPath + "skinBlue.skin", R.string.theme_blue, SkinThemeFactory2()
        )

    object Black :
        WanDynamicTheme(
            "com.pp.skin", skinPath + "skinBlack.skin", R.string.theme_black, SkinThemeFactory2()
        )
}




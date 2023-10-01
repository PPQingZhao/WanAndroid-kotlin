package com.pp.common.theme

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.util.DisplayMetrics
import androidx.annotation.StringRes
import com.pp.common.app.App
import com.pp.common.constant.getSkin
import com.pp.theme.DynamicTheme
import com.pp.theme.DynamicThemeManager
import com.pp.theme.factory.DefaultThemeInfoFactory
import com.pp.theme.factory.SkinThemeInfoFactory
import com.pp.ui.R

sealed class WanDynamicTheme(
    skinPath: String,
    @StringRes name: Int,
    private val factory: DynamicThemeManager.ThemeFactory,
) :
    DynamicThemeManager.ApplySkinTheme(skinPath, name) {

    // 获取皮肤包 包名
    open fun getPackageName(skinPath: String): String? {
        return App.getInstance().packageManager.getPackageArchiveInfo(
            skinPath,
            PackageManager.GET_ACTIVITIES
        )?.packageName
    }

    override fun create(
        displayMetrics: DisplayMetrics,
        configuration: Configuration,
        themeName: String,
    ): DynamicTheme.Info? {

        val packageName = getPackageName(skinPath) ?: return null

        val theme = factory.create(
            displayMetrics = displayMetrics,
            configuration = configuration,
            themePackage = packageName,
            themeName = themeName,
            skinPath = skinPath
        ) ?: return null

        return DynamicTheme.Info(this, theme, packageName)
    }

    @SuppressLint("StaticFieldLeak")
    object Default :
        WanDynamicTheme(
            "",
            R.string.theme_default,
            DefaultThemeInfoFactory(App.getInstance())
        ) {
        override fun getPackageName(skinPath: String): String {
            return App.getInstance().packageName
        }
    }

    object Blue :
        WanDynamicTheme(
            getSkin("skinBlue.skin"),
            R.string.theme_blue,
            SkinThemeInfoFactory()
        )

    object Black :
        WanDynamicTheme(
            getSkin("skinBlack.skin"),
            R.string.theme_black,
            SkinThemeInfoFactory()
        )
}




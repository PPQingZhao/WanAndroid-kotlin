package com.pp.theme.factory

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.util.DisplayMetrics
import android.util.Log
import com.pp.theme.DynamicThemeManager

class DefaultThemeInfoFactory(private val context: Context) : DynamicThemeManager.ThemeFactory {
    private val DEBUG = false
    @SuppressLint("DiscouragedApi")
    override fun create(
        displayMetrics: DisplayMetrics,
        configuration: Configuration,
        skinPath: String,
        themePackage: String,
        themeName: String,
    ): Theme? {
        val theme = kotlin.runCatching {
            if (DEBUG) {
                Log.e(
                    "ThemeFactory",
                    "create theme: {skinFile: $skinPath, defPackage: $themePackage,themeName:$themeName}"
                )
            }

            val skinResources = Resources(context.assets, displayMetrics, configuration)

            val themeId = skinResources.getIdentifier(themeName, "style", themePackage)
            if (DEBUG) {
                Log.e("ThemeFactory", "themeId: $themeId")
            }
            if (themeId > 0) {
                val skinTheme = skinResources.newTheme()
                skinTheme.applyStyle(themeId, true)
                skinTheme
            } else null
        }.getOrNull()

        return theme
    }


}
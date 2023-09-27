package com.pp.theme.factory

import android.annotation.SuppressLint
import android.content.res.AssetManager
import android.content.res.Configuration
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.util.DisplayMetrics
import android.util.Log
import com.pp.theme.DynamicThemeManager
import java.lang.reflect.Method

/**
 * 皮肤包主题工厂
 */
class SkinThemeFactory2 : DynamicThemeManager.ThemeFactory {
    private val assetManager = AssetManager::class.java.newInstance()

    @SuppressLint("DiscouragedPrivateApi")
    private fun addAssetPath(skinPath: String): Method {
        val addAssetPathMethod =
            AssetManager::class.java.getDeclaredMethod("addAssetPath", String::class.java)
        addAssetPathMethod.isAccessible = true
        addAssetPathMethod.invoke(assetManager, skinPath)
        return addAssetPathMethod
    }

    @SuppressLint("DiscouragedPrivateApi", "DiscouragedApi")
    override fun create(
        defaultTheme: Theme,
        skinPath: String,
        themePackage: String,
        themeName: String,
    ): Resources.Theme {

        val theme = kotlin.runCatching {
            Log.e(
                "ThemeFactory",
                "create theme: {skinFile: $skinPath, defPackage: $themePackage themeName:$themeName}"
            )

            addAssetPath(skinPath)

            val displayMetrics = DisplayMetrics().apply {
                setTo(defaultTheme.resources.displayMetrics)
            }
            val configuration = Configuration().apply {
                setTo(defaultTheme.resources.configuration)
            }
            val skinResources =
                Resources(assetManager, displayMetrics, configuration)

            val themeId =
                skinResources.getIdentifier(themeName, "style", themePackage)
            Log.e("ThemeFactory", "themeId: $themeId")
            if (themeId > 0) {
                val skinTheme = skinResources.newTheme()
                skinTheme.applyStyle(themeId, true)
                skinTheme
            } else null
        }.getOrNull()

        return theme ?: defaultTheme
    }


}
package com.pp.theme.factory

import android.annotation.SuppressLint
import android.content.res.AssetManager
import android.content.res.Configuration
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.util.DisplayMetrics
import android.util.Log

/**
 * 皮肤包主题工厂
 */
class SkinThemeFactory : Factory<Theme?> {
    private val displayMetrics: DisplayMetrics
    private val configuration: Configuration

    // 皮肤存储路径
    private val skinFile: String

    // 主题名称(字符串形式)
    private val themeName: String

    // 皮肤包 package
    private val defPackage: String

    constructor(
        skinFile: String,
        displayMetrics: DisplayMetrics,
        configuration: Configuration,
        themeName: String,
        defPackage: String,
    ) {
        this.skinFile = skinFile
        this.displayMetrics = DisplayMetrics().apply {
            setTo(displayMetrics)
        }
        this.configuration = Configuration().apply {
            setTo(configuration)
        }
        this.themeName = themeName
        this.defPackage = defPackage
    }


    @SuppressLint("DiscouragedPrivateApi", "DiscouragedApi")
    override fun create(): Resources.Theme? {

        return kotlin.runCatching {
            Log.e(
                "ThemeFactory",
                "create theme: {skinFile: $skinFile, defPackage: $defPackage themeName:$themeName}"
            )
            val assetManager = AssetManager::class.java.newInstance()
            val addAssetPathMethod =
                AssetManager::class.java.getDeclaredMethod("addAssetPath", String::class.java)
            addAssetPathMethod.isAccessible = true
            addAssetPathMethod.invoke(assetManager, skinFile)

            val skinResources =
                Resources(assetManager, displayMetrics, configuration)

            val themeId =
                skinResources.getIdentifier(themeName, "style", defPackage)
            Log.e("ThemeFactory","themeId: $themeId")
            val skinTheme = skinResources.newTheme()
            skinTheme.applyStyle(themeId, true)
            skinTheme
        }.getOrNull()
    }
}
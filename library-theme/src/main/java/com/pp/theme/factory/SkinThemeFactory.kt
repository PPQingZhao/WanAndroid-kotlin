package com.pp.theme.factory

import android.annotation.SuppressLint
import android.content.res.AssetManager
import android.content.res.Configuration
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.util.DisplayMetrics
import com.pp.theme.R

/**
 * 皮肤包主题工厂
 */
class SkinThemeFactory : Factory<Theme?> {
    private val displayMetrics: DisplayMetrics
    private val configuration: Configuration

    // 皮肤存储路径
    private val skinPath: String

    // 主题名称(字符串形式)
    private val themeName: String

    // 皮肤包 package
    private val defPackage: String

    constructor(
        skinPath: String,
        displayMetrics: DisplayMetrics,
        configuration: Configuration,
        themeName: String,
        defPackage: String,
    ) {
        this.skinPath = skinPath
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
            val assetManager = AssetManager::class.java.newInstance()
            val addAssetPathMethod =
                AssetManager::class.java.getDeclaredMethod("addAssetPath", String::class.java)
            addAssetPathMethod.isAccessible = true
            addAssetPathMethod.invoke(assetManager, skinPath)

            val skinResources =
                Resources(assetManager, displayMetrics, configuration)

            val themeId =
                skinResources.getIdentifier(themeName, "style", defPackage)
            val skinTheme = skinResources.newTheme()
            skinTheme.applyStyle(themeId, true)
            skinTheme
        }.getOrNull()
    }
}
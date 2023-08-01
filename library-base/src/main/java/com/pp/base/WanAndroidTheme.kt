package com.pp.base

import android.content.res.Configuration
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import androidx.annotation.IntDef
import androidx.annotation.RestrictTo
import com.pp.theme.DynamicTheme
import com.pp.theme.DynamicThemeManager
import com.pp.theme.factory.SkinThemeFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
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

/**
 * 初始化 DynamicTheme
 */
fun attachTheme(
    dynamicTheme: DynamicTheme,
    defaultTheme: Theme,
    displayMetrics: DisplayMetrics,
    configuration: Configuration,
    themeName: String = "Theme.Dynamic",
    themePackage: String = "com.pp.skin",
    skin: String = "",
) {
    CoroutineScope(Dispatchers.IO).launch {
        // 缓存的主题发生变化
        getPreferenceTheme().collectLatest { themeId ->
            // 创建 WanAndroidTheme
            val wanAndroidTheme = WanAndroidTheme.getThemeById(
                themeId ?: WanAndroidTheme.Default,
                defaultTheme,
                displayMetrics,
                configuration,
                themeName,
                themePackage,
                skin
            )
            // 更新 dynamicTheme
            withContext(Dispatchers.Main) {
                dynamicTheme.applyTheme(wanAndroidTheme.getTheme())
            }
        }
    }
}

/**
 * 更新主题
 */
fun updateTheme(@WanAndroidTheme.ThemeId themeId: Int) {
    DynamicThemeManager.updateTheme(themeId)
}

/**
 * 本地缓存主题发射器
 */
fun getPreferenceTheme(): Flow<Int?> {
    return DynamicThemeManager.getPreferenceTheme()
}

/**
 * 主题类型
 */
sealed class WanAndroidTheme(val defaultTheme: Theme) {
    companion object {
        const val Default = 0
        const val Black = 1
        const val Blue = 2

        fun getThemeById(
            @ThemeId themeId: Int?,
            defaultTheme: Theme,
            displayMetrics: DisplayMetrics,
            configuration: Configuration,
            themeName: String = "Theme.Dynamic",
            themePackage: String = "com.pp.skin",
            skin: String = "",
        ): WanAndroidTheme {
            return when (themeId) {
                Black -> {
                    Black(
                        defaultTheme,
                        displayMetrics,
                        configuration,
                        themeName,
                        themePackage,
                        skin.ifEmpty { "skinBlack.skin" }
                    )
                }
                Blue -> {
                    Blue(
                        defaultTheme,
                        displayMetrics,
                        configuration,
                        themeName,
                        themePackage,
                        skin.ifEmpty { "skinBlue.skin" }
                    )
                }
                else -> {
                    Default(defaultTheme)
                }
            }
        }
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
    @IntDef(Default, Black, Blue)
    @Retention(AnnotationRetention.SOURCE)
    annotation class ThemeId

    abstract fun getTheme(): Theme

    abstract fun getThemeId(): Int

    class Default(defaultTheme: Resources.Theme) :
        WanAndroidTheme(defaultTheme) {
        override fun getTheme(): Theme {
            return defaultTheme
        }

        override fun getThemeId(): Int {
            return Default
        }
    }

    class Black(
        defaultTheme: Resources.Theme,
        private val displayMetrics: DisplayMetrics,
        private val configuration: Configuration,
        private val themeName: String = "Theme.Dynamic",
        private val themePackage: String = "com.pp.skin",
        private val skin: String = "skinBlack.skin",
    ) : WanAndroidTheme(defaultTheme) {
        override fun getTheme(): Resources.Theme {
            return SkinThemeFactory(
                skinPath + skin,
                displayMetrics,
                configuration,
                themeName,
                themePackage
            ).create() ?: defaultTheme
        }

        override fun getThemeId(): Int {
            return Black
        }
    }

    class Blue(
        defaultTheme: Resources.Theme,
        private val displayMetrics: DisplayMetrics,
        private val configuration: Configuration,
        private val themeName: String = "Theme.Dynamic",
        private val themePackage: String = "com.pp.skin",
        private val skin: String = "skinBlue.skin",
    ) : WanAndroidTheme(defaultTheme) {
        override fun getTheme(): Resources.Theme {
            return SkinThemeFactory(
                skinPath + skin,
                displayMetrics,
                configuration,
                themeName,
                themePackage
            ).create() ?: defaultTheme
        }

        override fun getThemeId(): Int {
            return Blue
        }
    }
}

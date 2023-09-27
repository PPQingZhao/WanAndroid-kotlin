package com.pp.base

import android.content.res.Configuration
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.os.Environment
import android.util.DisplayMetrics
import androidx.annotation.IntDef
import androidx.annotation.RestrictTo
import androidx.annotation.StringRes
import com.pp.theme.factory.SkinThemeFactory
import com.pp.ui.R
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
 * 主题类型
 */
sealed class WanAndroidTheme(
    protected val defaultTheme: Theme,
    protected val themePackage: String = "com.pp.skin",
) {
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
                        themePackage,
                        displayMetrics,
                        configuration,
                        themeName,
                        skin.ifEmpty { "skinBlack.skin" }
                    )
                }
                Blue -> {
                    Blue(
                        defaultTheme,
                        themePackage,
                        displayMetrics,
                        configuration,
                        themeName,
                        skin.ifEmpty { "skinBlue.skin" }
                    )
                }
                else -> {
                    Default(defaultTheme, "com.pp.wanandroid")
                }
            }
        }
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
    @IntDef(Default, Black, Blue)
    @Retention(AnnotationRetention.SOURCE)
    annotation class ThemeId

    abstract fun getTheme(): Theme

    abstract fun getPackage(): String

    @StringRes
    abstract fun getName(): Int

    class Default(defaultTheme: Resources.Theme, themePackage: String = "com.pp.wanandroid") :
        WanAndroidTheme(defaultTheme, themePackage) {
        override fun getTheme(): Theme {
            return defaultTheme
        }

        override fun getPackage(): String {
            return themePackage
        }

        override fun getName(): Int {
            return R.string.theme_default
        }
    }

    class Black(
        defaultTheme: Resources.Theme,
        themePackage: String = "com.pp.skin",
        private val displayMetrics: DisplayMetrics,
        private val configuration: Configuration,
        private val themeName: String = "Theme.Dynamic",
        private val skin: String = "skinBlack.skin",
    ) : WanAndroidTheme(defaultTheme, themePackage) {
        override fun getTheme(): Resources.Theme {
            return SkinThemeFactory(
                skinPath + skin,
                displayMetrics,
                configuration,
                themeName,
                themePackage
            ).create() ?: defaultTheme
        }

        override fun getPackage(): String {
            return themePackage
        }

        override fun getName(): Int {
            return R.string.theme_black
        }
    }

    class Blue(
        defaultTheme: Resources.Theme,
        themePackage: String = "com.pp.skin",
        private val displayMetrics: DisplayMetrics,
        private val configuration: Configuration,
        private val themeName: String = "Theme.Dynamic",
        private val skin: String = "skinBlue.skin",
    ) : WanAndroidTheme(defaultTheme, themePackage) {
        override fun getTheme(): Resources.Theme {
            return SkinThemeFactory(
                skinPath + skin,
                displayMetrics,
                configuration,
                themeName,
                themePackage
            ).create() ?: defaultTheme
        }

        override fun getPackage(): String {
            return themePackage
        }

        override fun getName(): Int {
            return R.string.theme_blue
        }
    }
}

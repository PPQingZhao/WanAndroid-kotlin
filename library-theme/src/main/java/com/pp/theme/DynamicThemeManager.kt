package com.pp.theme

import android.content.Context
import android.content.res.Resources
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

/**
 * 动态主题管理类:
 * 更新本地主题
 * 获取本地主题
 */
object DynamicThemeManager {

    private const val THEME_SETTING = "theme_setting"
    private val Context.themeDataStore by preferencesDataStore(name = THEME_SETTING)
    private val themeKey by lazy { intPreferencesKey("theme") }

    private var sContext: Context? = null
        set(value) {
            check(null == field) { "Do not initialize AppDataBase again" }
            field = value
        }
        get() {
            return checkNotNull(field) { "you should call DynamicThemeManager.init(context) at first" }
        }

    fun init(ctx: Context) {
        sContext = ctx
    }

    /**
     * 缓存主题
     */
    private suspend fun Context.setPreferenceTheme(theme: Int) {
        themeDataStore.edit {
            it[themeKey] = theme
        }
    }

    /**
     * 获取缓存的主题
     */
    private fun Context.getPreferenceTheme(): Flow<Int?> {
        return themeDataStore.data.map {
            it[themeKey]
        }
    }

    fun getPreferenceTheme(): Flow<Int?> {
        return sContext!!.getPreferenceTheme()
    }

    /**
     * 更新主题
     */
    suspend fun updateTheme(@androidx.annotation.IntRange(from = 0, to = 100) themeId: Int) {
        val cacheThemeId = getPreferenceTheme().firstOrNull()
        // 与缓存的themeId比较,相同则不处理
        if (cacheThemeId == themeId) {
            // The same theme
            return
        }
        sContext!!.setPreferenceTheme(themeId)
    }

    interface ThemeInfoFactory {
        fun create(themeId: Int?): DynamicTheme.Info
    }

}
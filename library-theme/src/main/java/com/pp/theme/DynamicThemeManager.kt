package com.pp.theme

import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.annotation.StringRes
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
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
    private val skinPreferenceKey by lazy { stringPreferencesKey("theme") }
    private var defaultApplySkinTheme: ApplySkinTheme? = null
        set(value) {
            check(field == null) { "The default Apply Skin Theme is already set." }
            field = value
        }
        get() {
            checkNotNull(field) { "you should call DynamicThemeManager.init(context) at first." }
            return field
        }

    // 皮肤包集
    private val skinMap = mutableMapOf<String, ApplySkinTheme>()

    private var sContext: Context? = null
        set(value) {
            check(null == field) { "Don't initialize context again" }
            field = value
        }
        get() {
            return checkNotNull(field) { "you should call DynamicThemeManager.init(context) at first." }
        }

    fun init(ctx: Context, default: ApplySkinTheme): DynamicThemeManager {
        sContext = ctx
        defaultApplySkinTheme = default
        return addSkinInfo(default)
    }

    /**
     * 添加皮肤包
     */
    fun addSkinInfo(skinTheme: ApplySkinTheme): DynamicThemeManager {
        if (skinMap.containsKey(skinTheme.skinPath)) throw RuntimeException("Adding repeated dynamic theme is not supported.")

        skinMap[skinTheme.skinPath] = skinTheme
        return this
    }

    /**
     * 删除皮肤包
     */
    fun removeSkinInfo(info: ApplySkinTheme) {
        skinMap.remove(info.skinPath)
    }

    fun getSkinTheme(): Collection<ApplySkinTheme> {
        return skinMap.values
    }


    /**
     * 缓存主题
     */
    private suspend fun Context.setPreferenceSkinTheme(theme: String) {
        themeDataStore.edit {
            it[skinPreferenceKey] = theme
        }
    }

    /**
     * 获取缓存的主题
     */
    private fun Context.getPreferenceSkinTheme(): Flow<ApplySkinTheme> {
        return themeDataStore.data.map {
            val key = it[skinPreferenceKey]
            Log.e("TAG", "$key")
            skinMap.get(key) ?: defaultApplySkinTheme!!
        }
    }

    internal fun getPreferenceSkinTheme(): Flow<ApplySkinTheme> {
        return sContext!!.getPreferenceSkinTheme()
    }


    /**
     * 更新主题
     */
    internal suspend fun updateTheme(info: ApplySkinTheme) {
        val cacheSkin = getPreferenceSkinTheme().firstOrNull()
        // 与缓存的themeId比较,相同则不处理
        if (cacheSkin?.skinPath == info.skinPath) {
            // The same theme
            return
        }
        sContext!!.setPreferenceSkinTheme(info.skinPath)
    }

    interface ThemeFactory {
        fun create(
            defaultTheme: Resources.Theme,
            skinPath: String,
            themePackage: String,
            themeName: String,
        ): Resources.Theme
    }

    /**
     * 皮肤包信息
     */
    abstract class ApplySkinTheme(
        val skinPackage: String, val skinPath: String, @StringRes val name: Int,
    ) {

        suspend fun applySkinTheme() {
            updateTheme(this)
        }

        abstract fun create(
            defaultTheme: Resources.Theme,
            themeName: String,
        ): Resources.Theme?

    }
}

fun listenerSkinTheme(): Flow<DynamicThemeManager.ApplySkinTheme> {
    return DynamicThemeManager.getPreferenceSkinTheme()
}
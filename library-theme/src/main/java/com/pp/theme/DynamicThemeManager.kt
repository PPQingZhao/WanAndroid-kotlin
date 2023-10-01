package com.pp.theme

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.DisplayMetrics
import androidx.annotation.StringRes
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * 动态主题管理类:
 * 更新本地主题
 * 获取本地主题
 */
object DynamicThemeManager {

    private const val THEME_SETTING = "theme_setting"
    private val Context.themeDataStore by preferencesDataStore(name = THEME_SETTING)
    private val skinPreferenceKey by lazy { stringPreferencesKey("theme") }
    private var currentApplySkinTheme: ApplySkinTheme? = null
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

    fun init(scope: CoroutineScope, ctx: Context, default: ApplySkinTheme): DynamicThemeManager {
        sContext = ctx
        defaultApplySkinTheme = default
        currentApplySkinTheme = defaultApplySkinTheme
        scope.launch {

            getPreferenceSkinTheme().collectLatest { applySkinTheme ->
                currentApplySkinTheme = applySkinTheme
                dynamicThemeList.onEach {
                    it.applySkinTheme(applySkinTheme)
                }
            }
        }

        return addSkinInfo(default)
    }

    private val dynamicThemeList = mutableListOf<DynamicTheme>()
    fun getDynamicTheme(): List<DynamicTheme> {
        return dynamicThemeList
    }

    fun addDynamicTheme(dynamicTheme: DynamicTheme) {
        currentApplySkinTheme?.run {
            dynamicTheme.applySkinTheme(this)
        }

        dynamicThemeList.add(dynamicTheme)
    }

    /**
     * 添加皮肤包
     */
    fun addSkinInfo(skinTheme: ApplySkinTheme): DynamicThemeManager {
        if (skinMap.containsKey(skinTheme.skinPath)) throw RuntimeException("Adding repeated skin theme is not supported.")

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
            skinMap.get(key) ?: defaultApplySkinTheme!!
        }
    }

    internal fun getPreferenceSkinTheme(): Flow<ApplySkinTheme> {
        return sContext!!.getPreferenceSkinTheme()
    }

    fun getCurrentApplySkinTheme(): ApplySkinTheme {
        return currentApplySkinTheme!!
    }

    /**
     * 更新主题
     */
    suspend fun applySkinTheme(info: ApplySkinTheme) {
        val cacheSkin = getCurrentApplySkinTheme()
        // 与缓存的themeId比较,相同则不处理
        if (cacheSkin.skinPath == info.skinPath) {
            // The same skin
            return
        }
        sContext!!.setPreferenceSkinTheme(info.skinPath)
    }

    interface ThemeFactory {
        fun create(
            displayMetrics: DisplayMetrics,
            configuration: Configuration,
            skinPath: String,
            themePackage: String,
            themeName: String,
        ): Resources.Theme?
    }

    /**
     * 皮肤包信息
     */
    abstract class ApplySkinTheme(
        val skinPath: String, @StringRes val name: Int,
    ) {

        abstract fun create(
            displayMetrics: DisplayMetrics,
            configuration: Configuration,
            themeName: String,
        ): DynamicTheme.Info?
    }
}

fun listenerSkinTheme(): Flow<DynamicThemeManager.ApplySkinTheme> {
    return DynamicThemeManager.getPreferenceSkinTheme()
}

inline fun <reified Theme : DynamicTheme> getDynamicTheme(
    themeName: String,
    displayMetrics: DisplayMetrics,
    configuration: Configuration,
): Theme {
    val themeClass = Theme::class.java

    // todo: 复用 DynamicTheme 会导致 StateView出现重影bug，待解决
    /* DynamicThemeManager.getDynamicTheme().apply {
         Log.e("TAG", "$themeName  getDynamicTheme size: ${size}")
     }.onEach { dynamicTheme ->
         if (dynamicTheme::class.java == themeClass
             && dynamicTheme.getThemeName() == themeName
             && dynamicTheme.getDisplayMetrics() == displayMetrics
             && dynamicTheme.getConfiguration() == configuration
         ) {
             Log.e("TAG", "getDynamicTheme 1111111111111")
             return dynamicTheme as Theme
         }
     }*/
    return themeClass.getDeclaredConstructor(
        DisplayMetrics::class.java,
        Configuration::class.java,
        String::class.java
    ).newInstance(displayMetrics, configuration, themeName).apply {
        DynamicThemeManager.addDynamicTheme(this)
//        Log.e("TAG", "getDynamicTheme 2222222222222222222")
    }
}
package com.pp.theme

import android.content.res.Resources.Theme
import com.pp.theme.DynamicThemeManager.getPreferenceTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext

/**
 * 动态主题
 */
abstract class DynamicTheme {
    abstract fun applyTheme(theme: Theme)

}

suspend fun DynamicTheme.collectTheme(
    factory: DynamicThemeManager.ThemeFactory,
) {
    // 缓存的主题发生变化
    getPreferenceTheme().collectLatest { themeId ->
        // 创建 WanAndroidTheme
        val theme = factory.create(themeId)
        // 更新 dynamicTheme
        withContext(Dispatchers.Main) {
            applyTheme(theme)
        }
    }

}
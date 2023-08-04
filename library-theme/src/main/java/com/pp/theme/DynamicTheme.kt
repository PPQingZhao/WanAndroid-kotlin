package com.pp.theme

import android.content.res.Resources.Theme
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.lifecycle.MutableLiveData
import com.pp.theme.DynamicThemeManager.getPreferenceTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext

/**
 * 动态主题
 */
abstract class DynamicTheme {
    private val _themeInfo = MutableLiveData<Info>()
    val themeInfo = _themeInfo

    fun setInfo(info: Info) {
        info.run {
            _themeInfo.value = this
            applyTheme(theme)
        }
    }

    abstract fun applyTheme(theme: Theme)

    data class Info(val theme: Theme, val themePackage: String)
}

suspend fun DynamicTheme.collectTheme(
    factory: DynamicThemeManager.ThemeInfoFactory,
) {
    // 缓存的主题发生变化
    getPreferenceTheme().collectLatest { themeId ->
        // 创建 WanAndroidTheme
        val themeInfo = factory.create(themeId)
        // 更新 dynamicTheme
        withContext(Dispatchers.Main) {
            setInfo(themeInfo)
        }
    }
}

fun Theme.getColor(@AttrRes attrRes: Int, @ColorInt default: Int): Int {
    val typedArray = obtainStyledAttributes(arrayOf(attrRes).toIntArray())
    try {
        return typedArray.getColor(0, default)
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        typedArray.recycle()
    }
    return default
}
package com.pp.theme

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.content.res.Resources.Theme
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.util.Log
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData

/**
 * 动态主题
 */
abstract class DynamicTheme(
    private val displayMetrics: DisplayMetrics,
    private val configuration: Configuration,
    private val themeName: String,
) {

    fun getThemeName() = themeName
    fun getDisplayMetrics() = DisplayMetrics().apply { setTo(displayMetrics) }
    fun getConfiguration() = Configuration().apply { setTo(configuration) }

    companion object {
        private val drawable = ColorDrawable(Color.TRANSPARENT)
        private val colorState = ColorStateList.valueOf(Color.TRANSPARENT)
    }

    private val DEBUG = false

    private val _themeInfo = MutableLiveData<Info>()
    val themeInfo = _themeInfo

    private val colorStateListMap = mutableMapOf<String, MutableLiveData<ColorStateList>>()
    private val attrColorStateListMap = mutableMapOf<Int, MutableLiveData<ColorStateList>>()
    private val drawableMap = mutableMapOf<String, MutableLiveData<Drawable>>()
    private val attrDrawableMap = mutableMapOf<Int, MutableLiveData<Drawable>>()

    internal fun addColorStateList(
        colorName: String,
        resultColorStateList: MutableLiveData<ColorStateList>,
    ) {
        colorStateListMap[colorName] = resultColorStateList
    }

    internal fun addColorStateList(
        @AttrRes colorAttr: Int,
        resultColorStateList: MutableLiveData<ColorStateList>,
    ) {
        attrColorStateListMap[colorAttr] = resultColorStateList
    }

    internal fun addDrawable(
        @AttrRes drawableAttr: Int,
        resultDrawable: MutableLiveData<Drawable>,
    ) {
        attrDrawableMap[drawableAttr] = resultDrawable
    }

    internal fun addDrawable(
        drawableName: String,
        resultDrawable: MutableLiveData<Drawable>,
    ) {
        drawableMap[drawableName] = resultDrawable
    }

    internal fun setInfo(info: Info) {
        _themeInfo.value = info
        updateDrawable(info)
        updateAttrDrawable(info)
        updateColorStateList(info)
        updateAttrColorStateList(info)
        onSetInfo(info)
    }

    private fun updateAttrDrawable(info: Info) {
        if (info.theme.resources == null) {
            attrDrawableMap.values.onEach {
                it.value = drawable
            }
            return
        }
        val typedArray = info.theme.obtainStyledAttributes(attrDrawableMap.keys.toIntArray())
        attrDrawableMap.values.forEachIndexed { index, mutableLiveData ->
            try {
                mutableLiveData.value = typedArray.getDrawable(index)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        typedArray.recycle()
    }

    private fun updateAttrColorStateList(info: Info) {
        if (info.theme.resources == null) {
            attrColorStateListMap.values.onEach {
                it.value = colorState
            }
            return
        }
        val typedArray = info.theme.obtainStyledAttributes(attrColorStateListMap.keys.toIntArray())
        attrColorStateListMap.values.forEachIndexed { index, mutableLiveData ->
            try {
                mutableLiveData.value = typedArray.getColorStateList(index)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        typedArray.recycle()
    }

    fun onSetInfo(info: Info) {

    }


    @SuppressLint("DiscouragedApi")
    private fun updateColorStateList(info: Info) {
        if (info.theme.resources == null) {
            colorStateListMap.values.onEach {
                it.value = colorState
            }
            return
        }
        info.theme.resources.run {
            var attrName: String
            var resultColorStateList: MutableLiveData<ColorStateList>
            colorStateListMap.onEach {
                attrName = it.key
                val colorId = getIdentifier(attrName, "color", info.themePackage)
                if (DEBUG) {
                    Log.e("TAG", "updateColorStateList attrName: $attrName  id: $colorId")
                }
                resultColorStateList = it.value
                resultColorStateList.value =
                    ResourcesCompat.getColorStateList(this, colorId, info.theme)
            }
        }
    }

    @SuppressLint("DiscouragedApi")
    private fun updateDrawable(info: Info) {
        if (info.theme.resources == null) {
            drawableMap.values.onEach {
                it.value = drawable
            }
            return
        }
        info.theme.resources.run {
            var drawableName: String
            var resultDrawable: MutableLiveData<Drawable>
            drawableMap.onEach {
                drawableName = it.key
                val drawableId = getIdentifier(drawableName, "drawable", info.themePackage)
                if (DEBUG) {
                    Log.e("TAG", "updateDrawable drawableName: $drawableName  id: $drawableId")
                }
                resultDrawable = it.value
                resultDrawable.value = ResourcesCompat.getDrawable(this, drawableId, info.theme)
            }
        }
    }

    class Info(
        internal val info: DynamicThemeManager.ApplySkinTheme,
        val theme: Theme,
        val themePackage: String,
    )
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


/**
 * 主题切换更新DynamicTheme
 */
fun DynamicTheme.applySkinTheme(
    skinTheme: DynamicThemeManager.ApplySkinTheme,
) {
    val theme = skinTheme.create(getDisplayMetrics(), getConfiguration(), getThemeName())
    // 更新 dynamicTheme
    theme?.run {
        setInfo(DynamicTheme.Info(skinTheme, this, skinTheme.skinPackage))
    }
}
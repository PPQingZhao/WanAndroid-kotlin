package com.pp.theme

//noinspection SuspiciousImport
import android.R
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.content.res.Resources.Theme
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.activity.ComponentActivity
import androidx.annotation.CallSuper
import androidx.lifecycle.MutableLiveData

/**
 * 动态主题
 */
open class AppDynamicTheme : DynamicTheme() {

    companion object{
        private val drawable = ColorDrawable(Color.TRANSPARENT)
        private val colorState = ColorStateList.valueOf(Color.TRANSPARENT)
    }
    val windowBackground = MutableLiveData<Drawable?>(drawable)
    val colorPrimary = MutableLiveData<ColorStateList>(colorState)
    val colorAccent = MutableLiveData<ColorStateList>(colorState)
    val navigationBarColor = MutableLiveData<ColorStateList>(colorState)
    val textColorPrimary = MutableLiveData<ColorStateList>(colorState)
    val textColor = MutableLiveData<ColorStateList>(colorState)
    val textColorSecondary = MutableLiveData<ColorStateList>(colorState)
    val textColorTertiary = MutableLiveData<ColorStateList>(colorState)
    val textColorHint = MutableLiveData<ColorStateList>(colorState)
    val editTextColor = MutableLiveData<ColorStateList>(colorState)
    val progressTint = MutableLiveData<ColorStateList>(colorState)
    val secondaryProgressTint = MutableLiveData<ColorStateList>(colorState)
    val colorButtonNormal = MutableLiveData<ColorStateList>(colorState)
    val colorControlActivated = MutableLiveData<ColorStateList>(colorState)
    val strokeColor = MutableLiveData<ColorStateList>(colorState)

    @CallSuper
    @SuppressLint("ResourceType", "Recycle")
    override fun applyTheme(theme: Theme) {

        val drawableAttrMap = mapOf(
            windowBackground to R.attr.windowBackground,
        )

        val colorAttrMap = mapOf(
            colorPrimary to R.attr.colorPrimary,
            colorAccent to R.attr.colorAccent,
            navigationBarColor to R.attr.navigationBarColor,
            textColorPrimary to R.attr.textColorPrimary,
            textColor to R.attr.textColor,
            textColorSecondary to R.attr.textColorSecondary,
            textColorTertiary to R.attr.textColorTertiary,
            textColorHint to R.attr.textColorHint,
            editTextColor to R.attr.editTextColor,
            progressTint to R.attr.progressTint,
            secondaryProgressTint to R.attr.secondaryProgressTint,
            colorButtonNormal to R.attr.colorButtonNormal,
            colorControlActivated to R.attr.colorControlActivated,
            strokeColor to R.attr.strokeColor,
        )

        val attrMap = drawableAttrMap + colorAttrMap

        val attrArr = attrMap.values.toIntArray()
        val typedArray = theme.obtainStyledAttributes(attrArr)

        val drawableStartIndex = 0
        drawableAttrMap.keys.forEachIndexed { index, mutableLiveData ->
            try {
                mutableLiveData.value = typedArray.getDrawable(drawableStartIndex + index)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val colorStartIndex = drawableAttrMap.size
        colorAttrMap.keys.forEachIndexed { index, mutableLiveData ->
            try {
                mutableLiveData.value = typedArray.getColorStateList(colorStartIndex + index)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        typedArray.recycle()
    }
}

fun AppDynamicTheme.init(activity: ComponentActivity): AppDynamicTheme {
    windowBackground.observe(activity) {
        // 主题窗口背景发生变化时,主动给window设置
        activity.window?.setBackgroundDrawable(it)
    }
    return this
}
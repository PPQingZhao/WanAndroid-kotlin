package com.pp.theme

//noinspection SuspiciousImport
import android.R
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.annotation.CallSuper
import androidx.lifecycle.MutableLiveData

/**
 * 动态主题
 */
open class AppDynamicTheme : DynamicTheme {

    val windowBackground = MutableLiveData<Drawable?>()
    val colorPrimary = MutableLiveData<ColorStateList>()
    val colorAccent = MutableLiveData<ColorStateList>()
    val textColor = MutableLiveData<ColorStateList>()
    val textColorSecondary = MutableLiveData<ColorStateList>()
    val textColorHint = MutableLiveData<ColorStateList>()
    val editTextColor = MutableLiveData<ColorStateList>()
    val progressTint = MutableLiveData<ColorStateList>()
    val secondaryProgressTint = MutableLiveData<ColorStateList>()
    val colorButtonNormal = MutableLiveData<ColorStateList>()
    val colorControlActivated = MutableLiveData<ColorStateList>()

    @CallSuper
    @SuppressLint("ResourceType", "Recycle")
    override fun applyTheme(theme: Resources.Theme) {

        val drawableAttrMap = mapOf(
            windowBackground to R.attr.windowBackground,
        )

        val colorAttrMap = mapOf(
            colorPrimary to R.attr.colorPrimary,
            colorAccent to R.attr.colorAccent,
            textColor to R.attr.textColor,
            textColorSecondary to R.attr.textColorSecondary,
            textColorHint to R.attr.textColorHint,
            editTextColor to R.attr.editTextColor,
            progressTint to R.attr.progressTint,
            secondaryProgressTint to R.attr.secondaryProgressTint,
            colorButtonNormal to R.attr.colorButtonNormal,
            colorControlActivated to R.attr.colorControlActivated,
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
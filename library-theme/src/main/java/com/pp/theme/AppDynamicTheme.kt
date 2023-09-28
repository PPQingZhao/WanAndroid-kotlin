package com.pp.theme

//noinspection SuspiciousImport
import android.R
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.content.res.Resources.Theme
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import androidx.activity.ComponentActivity
import androidx.annotation.CallSuper
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData

/**
 * 动态主题
 */
open class AppDynamicTheme internal constructor(
    displayMetrics: DisplayMetrics,
    configuration: Configuration,
    themeName: String,
) : DynamicTheme(displayMetrics, configuration, themeName) {

    companion object {
        private val drawable = ColorDrawable(Color.TRANSPARENT)
        private val colorState = ColorStateList.valueOf(Color.TRANSPARENT)
    }

    val windowBackground = MutableLiveData<Drawable>(drawable)
    val colorPrimary = MutableLiveData<ColorStateList>(colorState)
    val colorPrimaryVariant = MutableLiveData<ColorStateList>(colorState)
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

    init {
//        windowBackground to R.attr.windowBackground,
        addDrawable(R.attr.windowBackground, windowBackground)

//        colorPrimaryVariant to com.google.android.material.R.attr.colorPrimaryVariant,
        addColorStateList("colorPrimaryVariant", colorPrimaryVariant)

        /*
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
        */
        addColorStateList(R.attr.colorPrimary, colorPrimary)
        addColorStateList(R.attr.colorAccent, colorAccent)
        addColorStateList(R.attr.navigationBarColor, navigationBarColor)
        addColorStateList(R.attr.textColorPrimary, textColorPrimary)
        addColorStateList(R.attr.textColor, textColor)
        addColorStateList(R.attr.textColorSecondary, textColorSecondary)
        addColorStateList(R.attr.textColorTertiary, textColorTertiary)
        addColorStateList(R.attr.textColorHint, textColorHint)
        addColorStateList(R.attr.editTextColor, editTextColor)
        addColorStateList(R.attr.progressTint, progressTint)
        addColorStateList(R.attr.secondaryProgressTint, secondaryProgressTint)
        addColorStateList(R.attr.colorButtonNormal, colorButtonNormal)
        addColorStateList(R.attr.colorControlActivated, colorControlActivated)
        addColorStateList(R.attr.strokeColor, strokeColor)

    }
}

fun AppDynamicTheme.init(activity: ComponentActivity): AppDynamicTheme {
    windowBackground.observe(activity) {
        // 主题窗口背景发生变化时,主动给window设置
        activity.window?.setBackgroundDrawable(it)
    }
    return this
}
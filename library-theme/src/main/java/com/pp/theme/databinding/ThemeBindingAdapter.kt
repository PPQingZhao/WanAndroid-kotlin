package com.pp.theme.databinding

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import com.pp.theme.DynamicTheme
import com.pp.theme.getColor

object ThemeBindingAdapter {
    private const val TAG = "ThemeBindingAdapter"

    @JvmStatic
    @SuppressLint("ResourceAsColor")
    @androidx.databinding.BindingAdapter(
        value = ["dynamicTheme", "textColorAttr"],
        requireAll = true
    )
    fun setTextColor(view: TextView, themeInfo: DynamicTheme.Info?, @AttrRes attrRes: Int) {
//        Log.e(TAG, "res: $attrRes")
        themeInfo?.theme?.getColor(attrRes, Color.TRANSPARENT)?.run {
            view.setTextColor(this)
        }
    }

    @JvmStatic
    @SuppressLint("ResourceAsColor", "UseCompatLoadingForDrawables")
    @androidx.databinding.BindingAdapter(
        value = ["dynamicTheme", "drawableName"],
        requireAll = true
    )
    fun setImageRes(view: ImageView, themeInfo: DynamicTheme.Info?, drawableName: String) {
        Log.e(TAG, "drawableName: $drawableName")
        themeInfo?.theme?.resources?.run {
            val drawableId = getIdentifier(drawableName, "drawable", themeInfo.themePackage)
            Log.e(TAG, "drawableId: $drawableId")
            val drawable = getDrawable(drawableId, themeInfo.theme)
            view.setImageDrawable(drawable)
        }
    }
}

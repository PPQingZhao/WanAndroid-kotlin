package com.pp.theme.databinding

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.core.content.res.ResourcesCompat
import com.pp.theme.DynamicTheme
import com.pp.theme.getColor

private const val TAG = "ThemeBindingAdapter"

/**
 * 主题属性 textColorAttr:
 * 如:
 *  R.attr.textColor
 *  R.attr.colorAccent
 *
 *  <attr name="colorPrimary" format="color" />
<attr name="colorPrimaryDark" format="color" />
<attr name="colorSecondary" format="color" />
<attr name="colorAccent" format="color" />
 *  ...
 */
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

/**
 * 颜色资源名称 textColorName:
 * 如:
 *   <color name="colorPrimary">#FEFAF8</color>             ->  "colorPrimary"
 *   <color name="colorOnPrimary">#FEFAF8</color>           ->  "colorOnPrimary"
 *   <color name="colorPrimaryVariant">#FEFAF8</color>      ->  "colorPrimaryVariant"
 */
@SuppressLint("ResourceAsColor", "UseCompatLoadingForDrawables", "DiscouragedApi")
@androidx.databinding.BindingAdapter(
    value = ["dynamicThemeInfo", "textColorName"],
    requireAll = true
)
fun setTextColorRes(view: TextView, themeInfo: DynamicTheme.Info?, textColorName: String) {
    themeInfo?.theme?.resources?.run {
        val textColorId = getIdentifier(textColorName, "color", themeInfo.themePackage)
        val color = ResourcesCompat.getColorStateList(this, textColorId, themeInfo.theme)
        view.setTextColor(color)
    }
}

/**
 * drawableName: 图片资源名称
 * 如:
 * ic_launcher.png      -> “ic_launcher”
 * ic_arrow_back.png    -> “ic_arrow_back”
 */
@SuppressLint("ResourceAsColor", "UseCompatLoadingForDrawables", "DiscouragedApi")
@androidx.databinding.BindingAdapter(
    value = ["dynamicThemeInfo", "drawableName"],
    requireAll = true
)
fun setImageRes(view: ImageView, themeInfo: DynamicTheme.Info?, drawableName: String) {
//        Log.e(TAG, "drawableName: $drawableName")
    themeInfo?.theme?.resources?.run {
        val drawableId = getIdentifier(drawableName, "drawable", themeInfo.themePackage)
//            Log.e(TAG, "drawableId: $drawableId")
        val drawable = getDrawable(drawableId, themeInfo.theme)
        view.setImageDrawable(drawable)
    }
}

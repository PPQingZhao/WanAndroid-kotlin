package com.pp.ui.databinding

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.google.android.material.textfield.TextInputLayout
import com.pp.ui.utils.load

object BindingAdapter {


    @JvmStatic
    @androidx.databinding.BindingAdapter("android:background")
    fun setBackground(iv: View, color: ColorStateList) {
//        Log.e("TAG", "res: $res")
        iv.background = ColorDrawable(color.defaultColor)
    }

    @SuppressLint("ResourceType")
    @JvmStatic
    @androidx.databinding.BindingAdapter("android:text")
    fun setText(view: TextView, @StringRes resId: Int) {
//        Log.e("TAG", "res: $res")
        if (resId > 0) {
            view.setText(resId)
        } else {
            view.text = ""
        }
    }

    @JvmStatic
    @androidx.databinding.BindingAdapter("enable")
    fun setEnable(iv: View, enable: Boolean) {
//        Log.e("TAG", "res: $res")
        iv.isEnabled = enable
    }

    @JvmStatic
    @androidx.databinding.BindingAdapter("errorMessage")
    fun setErrorMessage(inputLayout: TextInputLayout, error: CharSequence) {
//        Log.e("TAG", "$error")
        inputLayout.error = error
    }

    @JvmStatic
    @androidx.databinding.BindingAdapter("helperText")
    fun setHelperText(inputLayout: TextInputLayout, text: CharSequence) {
//        Log.e("TAG", "$error")
        inputLayout.helperText = text
    }

    @JvmStatic
    @androidx.databinding.BindingAdapter("android:tint")
    fun setImageTint(iv: ImageView, color: ColorStateList?) {
        iv.imageTintList = color
    }

    @JvmStatic
    @androidx.databinding.BindingAdapter(
        value = ["android:imageUrl", "android:errorDrawable"],
        requireAll = true
    )
    fun setImageUrl(iv: ImageView, url: String?, error: Drawable) {
        iv.load(url, error)
    }

    @JvmStatic
    @androidx.databinding.BindingAdapter("android:src")
    fun setImageSrc(iv: ImageView, @DrawableRes src: Int) {
        iv.setImageResource(src)
    }

    @JvmStatic
    @androidx.databinding.BindingAdapter("android:webUrl")
    fun loadWebUrl(webView: WebView, webUrl: String?) {
        webUrl?.let {
            webView.loadUrl(it)
        }
    }


}
package com.pp.ui.viewModel

import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.databinding.ObservableInt
import com.pp.theme.AppDynamicTheme

open class ItemAllowRightViewModel(
    @DrawableRes icon: Int,
    @StringRes content: Int,
    val theme: AppDynamicTheme,
) {
    val icon = ObservableInt(icon)
    val content = ObservableInt(content)

    open fun onItemClick(v: View) {}
}
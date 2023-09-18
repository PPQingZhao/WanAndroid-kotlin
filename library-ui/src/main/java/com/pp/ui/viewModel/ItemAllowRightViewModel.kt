package com.pp.ui.viewModel

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.databinding.ObservableInt
import com.pp.theme.AppDynamicTheme

open class ItemAllowRightViewModel(
    @DrawableRes icon: Int,
    @StringRes content: Int,
    theme: AppDynamicTheme,
) : ItemDataViewModel<Any>(theme) {
    val icon = ObservableInt(icon)
    val content = ObservableInt(content)

}
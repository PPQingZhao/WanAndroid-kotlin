package com.pp.local.model

import androidx.databinding.ObservableInt
import com.pp.theme.AppDynamicTheme
import com.pp.ui.R
import com.pp.ui.viewModel.ItemDataViewModel

open class ItemThemeSettingViewModel<Data : Any>(
    theme: AppDynamicTheme,
) : ItemDataViewModel<Data>(theme) {
    val themeName = ObservableInt(R.string.theme_default)

}
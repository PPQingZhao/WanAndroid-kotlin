package com.pp.ui.viewModel

import androidx.databinding.ObservableInt
import com.pp.theme.AppDynamicTheme

open class ItemTextAllowRightViewModel<Data : Any>(
    theme: AppDynamicTheme,
) : ItemDataViewModel<Data>(theme) {
    val content = ObservableInt()

}
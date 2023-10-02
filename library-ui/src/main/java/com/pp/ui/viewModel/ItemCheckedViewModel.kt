package com.pp.ui.viewModel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import com.pp.theme.AppDynamicTheme

open class ItemCheckedViewModel<Data : Any>(
    theme: AppDynamicTheme,
) : ItemDataViewModel<Data>(theme) {
    val content = ObservableInt()
    val checked = ObservableBoolean()

}
package com.pp.ui.viewModel

import androidx.databinding.ObservableField
import com.pp.theme.AppDynamicTheme

open class ItemTextViewModel<Data : Any>(theme: AppDynamicTheme) :
    ItemDataViewModel<Data>(theme) {
    val text = ObservableField<String>()

}
package com.pp.ui.viewModel

import androidx.databinding.ObservableField
import com.pp.theme.AppDynamicTheme

open class ItemHistoryViewModel<Data : Any>(theme: AppDynamicTheme) :
    ItemDataViewModel<Data>(theme) {
    val title = ObservableField<String>()
    val time = ObservableField<String>()

}
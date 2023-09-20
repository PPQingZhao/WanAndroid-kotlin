package com.pp.ui.viewModel

import androidx.databinding.ObservableField
import com.pp.theme.AppDynamicTheme

open class ItemCoinReasonViewModel<Data : Any>(
    theme: AppDynamicTheme,
) : ItemDataViewModel<Data>(theme) {
    val coinReason = ObservableField<String>()
    val desc = ObservableField<String>()
    val coin = ObservableField<String>()


}
package com.pp.ui.viewModel

import androidx.databinding.ObservableField
import com.pp.theme.AppDynamicTheme

open class ItemCoinRankViewModel<Data : Any>(
    theme: AppDynamicTheme,
) : ItemDataViewModel<Data>(theme) {
    val rank = ObservableField<String>()
    val username = ObservableField<String>()
    val coinCount = ObservableField<String>()


}
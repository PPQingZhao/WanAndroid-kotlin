package com.pp.ui.viewModel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.pp.theme.AppDynamicTheme

open class ItemDeleteViewModel<Data : Any>(theme: AppDynamicTheme) :
    ItemDataViewModel<Data>(theme) {
    val text = ObservableField<String>()
    val isDeleteModel = MutableLiveData<Boolean>()
}
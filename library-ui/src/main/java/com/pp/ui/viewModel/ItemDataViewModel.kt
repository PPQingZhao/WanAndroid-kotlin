package com.pp.ui.viewModel

import android.view.View
import androidx.databinding.ObservableBoolean
import com.pp.theme.AppDynamicTheme

open class ItemDataViewModel<Data : Any>(val theme: AppDynamicTheme) {

    val isSelected = ObservableBoolean()
    protected var data: Data? = null

    fun updateData(data: Data?) {
        this.data = data
        onUpdateData(data)
    }

    open protected fun onUpdateData(data: Data?) {

    }

    open fun onItemClick(view: View) {
        isSelected.set(isSelected.get().not())
    }

}
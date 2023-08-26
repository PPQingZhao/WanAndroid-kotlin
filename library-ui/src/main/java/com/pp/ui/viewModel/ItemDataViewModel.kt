package com.pp.ui.viewModel

import android.view.View
import androidx.databinding.ObservableBoolean
import com.pp.theme.AppDynamicTheme

open class ItemDataViewModel<Data : Any>(val theme: AppDynamicTheme) {

    val isSelected = ObservableBoolean()
    protected var data: Data? = null

    private var position = -1

    fun setPosition(pos: Int) {
        position = pos
    }

    fun getPosition(): Int {
        return position
    }

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
package com.pp.ui.viewModel

import android.view.View
import androidx.databinding.ObservableBoolean
import com.pp.theme.AppDynamicTheme

open class ItemDataViewModel<Data : Any>(val theme: AppDynamicTheme) {

    val isSelected = ObservableBoolean()
    var data: (() -> Data?)? = null
        set(value) {
            if (field == value) {
                return
            }
            field = value

            onUpdateData(field?.invoke())
        }

    protected open fun onUpdateData(data: Data?) {

    }

    private var mOnItemListener: OnItemListener<ItemDataViewModel<Data>>? = null
    fun setOnItemListener(listener: OnItemListener<ItemDataViewModel<Data>>) {
        mOnItemListener = listener
    }

    fun onItemClick(view: View) {
        if (mOnItemListener?.onItemClick(view, this) == true) {
            return
        }
        onItemViewModelClick(view)
    }

    open fun onItemViewModelClick(view: View): Boolean {
        isSelected.set(isSelected.get().not())
        return false
    }

}
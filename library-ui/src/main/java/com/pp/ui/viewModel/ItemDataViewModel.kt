package com.pp.ui.viewModel

import android.view.View
import androidx.databinding.ObservableBoolean
import com.pp.theme.AppDynamicTheme

open class ItemDataViewModel<Data : Any>(val theme: AppDynamicTheme) {

    val isSelected = ObservableBoolean()
    var data: Data? = null
        set(value) {
            if (field == value) {
                return
            }
            field = value

            onUpdateData(data)
        }

    private var position = -1

    fun setPosition(pos: Int) {
        position = pos
    }

    fun getPosition(): Int {
        return position
    }

    protected open fun onUpdateData(data: Data?) {

    }

    private var mOnItemListener: OnItemListener<ItemDataViewModel<Data>>? = null
    fun setOnItemListener(listener: OnItemListener< ItemDataViewModel<Data>>) {
        mOnItemListener = listener
    }

    open fun onItemClick(view: View) {
        if (mOnItemListener?.onItemClick(view, this) == true) {
            return
        }
        isSelected.set(isSelected.get().not())
    }

}
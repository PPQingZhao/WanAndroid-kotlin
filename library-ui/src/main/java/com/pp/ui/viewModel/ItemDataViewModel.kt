package com.pp.ui.viewModel

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import com.pp.theme.AppDynamicTheme
import kotlinx.coroutines.launch

open class ItemDataViewModel<Data : Any>(
    val theme: AppDynamicTheme,
) {

    val isSelected = ObservableBoolean()
    var data: Data? = null
        set(value) {
            if (field == value) {
                return
            }
            field = value

            onUpdateData(field)
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
        ViewTreeLifecycleOwner.get(view)?.lifecycleScope?.launch {
            onItemViewModelClick(view)
        }
    }

    open suspend fun onItemViewModelClick(view: View): Boolean {
        isSelected.set(isSelected.get().not())
        return false
    }

}
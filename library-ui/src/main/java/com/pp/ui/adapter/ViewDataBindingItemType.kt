package com.pp.ui.adapter

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewTreeLifecycleOwner
import com.pp.ui.BR

abstract class ViewDataBindingItemType<VB : ViewDataBinding, VM : Any?, Data : Any> {
    companion object {
        val INVALID_TYPE = -1
    }

    abstract fun createViewDataBinding(parent: ViewGroup): VB
    fun bindItemViewModel(
        binding: VB,
        data: Data?,
        position: Int,
        cachedItemViewModel: VM?,
    ): VM {
        return onBindItemViewModel(binding, data, position, cachedItemViewModel).apply {
            setVariable(binding, this)
        }
    }

    abstract fun onBindItemViewModel(
        binding: VB,
        data: Data?,
        position: Int,
        cachedItemViewModel: VM?,
    ): VM

    fun setVariable(binding: VB, viewModel: VM) {
        try {
            val result = onSetVariable(binding, viewModel)
            if (!result) {
                //set default variable
                binding.setVariable(BR.viewModel, viewModel)
            }
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
    }

    open fun onSetVariable(binding: VB, viewModel: VM): Boolean {
        return false
    }

    fun onViewAttachedToWindow(binding: VB) {
        val lifecycleOwner = ViewTreeLifecycleOwner.get(binding.root)
        binding.lifecycleOwner = lifecycleOwner
    }

    open fun getItemType(): Int {
        return 0
    }

    open fun validItem(item: Data?): Boolean {
        return null != item
    }

    fun getItemType(item: Data?): Int {
        return if (validItem(item)) getItemType() else INVALID_TYPE
    }
}
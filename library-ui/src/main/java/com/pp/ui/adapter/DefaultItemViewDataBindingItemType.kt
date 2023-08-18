package com.pp.ui.adapter

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

class DefaultItemViewDataBindingItemType<VB : ViewDataBinding, VM : Any?, Data : Any>(
    private val onCreateViewDataBinding: (parent: ViewGroup) -> VB,
    private val onCreateItemViewModel: (binding: VB, data: Data?) -> VM,
    private val onSetVariable: (binding: VB, viewModel: VM) -> Boolean = { _, _ -> false },
) : ViewDataBindingItemType<VB, VM, Data>() {

    override fun createViewDataBinding(parent: ViewGroup): VB {
        return onCreateViewDataBinding.invoke(parent)
    }

    override fun onSetVariable(binding: VB, viewModel: VM): Boolean {
        return onSetVariable.invoke(binding, viewModel)
    }

    override fun createItemViewModel(binding: VB, data: Data?): VM {
        return onCreateItemViewModel.invoke(binding, data)
    }

}
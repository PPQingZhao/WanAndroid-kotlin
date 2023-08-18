package com.pp.ui.adapter

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

class DefaultViewDataBindingItemType<VB : ViewDataBinding, VM : Any?, Data : Any>(
    private val createBinding: (parent: ViewGroup) -> VB,
    private val createItemViewModel: (binding: VB, data: Data?) -> VM,
    private val onSetVariable: (binding: VB, viewModel: VM) -> Boolean = { _, _ -> false },
    private val getItemType: () -> Int = { 0 },
) : ViewDataBindingItemType<VB, VM, Data>() {

    override fun createViewDataBinding(parent: ViewGroup): VB {
        return createBinding.invoke(parent)
    }

    override fun onSetVariable(binding: VB, viewModel: VM): Boolean {
        return onSetVariable.invoke(binding, viewModel)
    }

    override fun createItemViewModel(binding: VB, data: Data?): VM {
        return createItemViewModel.invoke(binding, data)
    }

    override fun getItemType(): Int {
        return getItemType.invoke()
    }

}
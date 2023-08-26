package com.pp.ui.adapter

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

class DefaultViewDataBindingItemType<VB : ViewDataBinding, VM : Any?, Data : Any>(
    private val createBinding: (parent: ViewGroup) -> VB,
    private val onBindItemViewModel: (binding: VB, data: Data?, position: Int, cachedItemViewModel: VM?) -> VM,
    private val onSetVariable: (binding: VB, viewModel: VM) -> Boolean = { _, _ -> false },
    private val getItemType: () -> Int = { 0 },
    private val validItem: (data: Data?) -> Boolean = { null != it },
) : ViewDataBindingItemType<VB, VM, Data>() {

    override fun createViewDataBinding(parent: ViewGroup): VB {
        return createBinding.invoke(parent)
    }

    override fun onSetVariable(binding: VB, viewModel: VM): Boolean {
        return onSetVariable.invoke(binding, viewModel)
    }

    override fun onBindItemViewModel(
        binding: VB,
        data: Data?,
        position: Int,
        cachedItemViewModel: VM?
    ): VM {
        return onBindItemViewModel.invoke(binding, data, position, cachedItemViewModel)
    }

    override fun validItem(item: Data?): Boolean {
        return validItem.invoke(item)
    }

    override fun getItemType(): Int {
        return getItemType.invoke()
    }

}
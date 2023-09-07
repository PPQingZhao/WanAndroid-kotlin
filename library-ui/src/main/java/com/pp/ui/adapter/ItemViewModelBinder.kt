package com.pp.ui.adapter

import androidx.databinding.ViewDataBinding
import com.pp.ui.BR

interface ItemViewModelBinder<VB : ViewDataBinding, Data : Any> {
    fun getViewDataBindingClazz(): Class<VB>
    fun getDataClazz(): Class<Data>?
    fun bindViewModel(binding: VB, data: Data?, position: Int)
}

abstract class DefaultItemViewModelBinder<VB : ViewDataBinding, Data : Any, VM : Any>
    (private val onBindViewModel: (binding: VB, data: Data?, viewModel: VM, posiion: Int) -> Boolean = { _, _, _, _ -> false }) :
    ItemViewModelBinder<VB, Data> {

    private val mItemModelCaches by lazy { mutableMapOf<Int, VM>() }
    abstract fun getItemViewModel(data: Data?): VM

    override fun bindViewModel(binding: VB, data: Data?, position: Int) {
        var itemViewModel = mItemModelCaches[position]
        if (null == itemViewModel) {
            itemViewModel = getItemViewModel(data)
            mItemModelCaches[position] = itemViewModel
        }
        onBindViewModel(binding, data, itemViewModel, position)
    }

    open fun onBindViewModel(binding: VB, data: Data?, viewModel: VM, posiion: Int) {

        if (onBindViewModel.invoke(binding, data, viewModel, posiion)) {
            return
        }

        binding.setVariable(BR.viewModel, viewModel)
    }
}
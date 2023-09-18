package com.pp.ui.adapter

import androidx.databinding.ViewDataBinding
import com.pp.ui.viewModel.ItemDataViewModel
import com.pp.ui.viewModel.OnItemListener

class ItemDataViewModelBinder<VB : ViewDataBinding, Data : Any, VM : ItemDataViewModel<Data>>(
    private val getItemViewModel: (getItem: () -> Data?) -> VM,
    private val getViewDataBindingClazz: () -> Class<VB>,
    private val getDataClazz: () -> Class<Data>,
    onBindViewModel: (binding: VB, viewModel: VM?, position: Int, getItem: (position: Int) -> Data?) -> Boolean = { _, _, _, _ -> false },
    private val onItemListener: OnItemListener<ItemDataViewModel<Data>>? = null,
) : ItemViewModelBinder<VB, Data, VM>(onBindViewModel) {

    override fun onBindViewModel(
        binding: VB,
        viewModel: VM?,
        position: Int,
        getItem: (position: Int) -> Data?,
    ) {
        super.onBindViewModel(binding, viewModel, position, getItem)
        viewModel?.data = { getItem.invoke(position) }
        onItemListener?.also {
            viewModel?.setOnItemListener(it)
        }
    }

    override fun getItemViewModel(getItem: () -> Data?): VM {
        return getItemViewModel.invoke(getItem)
    }

    override fun getItemViewBindingClazz(): Class<VB> {
        return getViewDataBindingClazz.invoke()
    }

    override fun getItemDataClazz(): Class<Data> {
        return getDataClazz.invoke()
    }
}

inline fun <reified VB : ViewDataBinding, reified Data : Any, VM : ItemDataViewModel<Data>> createItemDataBinder(
    crossinline getItemViewModel: (getItem: () -> Data?) -> VM,
    onItemListener: OnItemListener<ItemDataViewModel<Data>>? = null,
    noinline onBindViewModel: (binding: VB, viewModel: VM?, position: Int, getItem: (position: Int) -> Data?) -> Boolean = { _, _, _, _ -> false },
) = ItemDataViewModelBinder<VB, Data, VM>(
    getItemViewModel = { getItemViewModel.invoke(it) },
    getViewDataBindingClazz = { VB::class.java },
    getDataClazz = { Data::class.java },
    onItemListener = onItemListener,
    onBindViewModel = onBindViewModel
)
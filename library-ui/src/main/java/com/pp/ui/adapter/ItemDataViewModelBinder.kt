package com.pp.ui.adapter

import androidx.databinding.ViewDataBinding
import com.pp.ui.viewModel.ItemDataViewModel
import com.pp.ui.viewModel.OnItemListener

class ItemDataViewModelBinder<VB : ViewDataBinding, Data : Any, VM : ItemDataViewModel<Data>>(
    private val getItemViewModel: (data: Data?) -> VM,
    private val getViewDataBindingClazz: () -> Class<VB>,
    private val getDataClazz: () -> Class<Data>,
    onBindViewModel: (binding: VB, data: Data?, viewModel: VM?, posiion: Int) -> Boolean = { _, _, _, _ -> false },
    private val onItemListener: OnItemListener<ItemDataViewModel<Data>>? = null,
) : ItemViewModelBinder<VB, Data, VM>(onBindViewModel) {

    companion object {
        inline fun <reified VB : ViewDataBinding, reified Data : Any, VM : ItemDataViewModel<Data>> createItemBinder(
            crossinline getItemViewModel: (data: Data?) -> VM,
            onItemListener: OnItemListener<ItemDataViewModel<Data>>? = null,
            noinline onBindViewModel: (binding: VB, data: Data?, viewModel: VM?, position: Int) -> Boolean = { _, _, _, _ -> false },
        ) = ItemDataViewModelBinder<VB, Data, VM>(
            getItemViewModel = { getItemViewModel.invoke(it) },
            getViewDataBindingClazz = { VB::class.java },
            getDataClazz = { Data::class.java },
            onItemListener = onItemListener,
            onBindViewModel = onBindViewModel
        )
    }

    override fun onBindViewModel(binding: VB, data: Data?, viewModel: VM?, posiion: Int) {
        super.onBindViewModel(binding, data, viewModel, posiion)
        viewModel?.data = data
        onItemListener?.also {
            viewModel?.setOnItemListener(it)
        }
    }

    override fun getItemViewModel(data: Data?): VM {
        return getItemViewModel.invoke(data)
    }

    override fun getItemViewBindingClazz(): Class<VB> {
        return getViewDataBindingClazz.invoke()
    }

    override fun getItemDataClazz(): Class<Data> {
        return getDataClazz.invoke()
    }
}

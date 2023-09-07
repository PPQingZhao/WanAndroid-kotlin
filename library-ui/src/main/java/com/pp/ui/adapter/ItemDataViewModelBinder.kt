package com.pp.ui.adapter

import androidx.databinding.ViewDataBinding
import com.pp.ui.viewModel.ItemDataViewModel
import com.pp.ui.viewModel.OnItemListener

class ItemDataViewModelBinder<VB : ViewDataBinding, Data : Any, VM : ItemDataViewModel<Data>>
    (
    private val getItemViewModel: (data: Data?) -> VM,
    private val getViewDataBindingClazz: () -> Class<VB>,
    private val getDataClazz: () -> Class<Data>,
    onBindViewModel: (binding: VB, data: Data?, viewModel: VM?, posiion: Int) -> Boolean = { _, _, _, _ -> false },
    private val onItemListener: OnItemListener<ItemDataViewModel<Data>>? = null,
) : DefaultItemViewModelBinder<VB, Data, VM>(onBindViewModel) {
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

    override fun getViewDataBindingClazz(): Class<VB> {
        return getViewDataBindingClazz.invoke()
    }

    override fun getDataClazz(): Class<Data> {
        return getDataClazz.invoke()
    }
}
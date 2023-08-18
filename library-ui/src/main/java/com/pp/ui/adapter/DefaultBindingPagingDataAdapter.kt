package com.pp.ui.adapter

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil

class DefaultBindingPagingDataAdapter<VB : ViewDataBinding, VM : Any?, Data : Any>(
    onCreateViewDataBinding: (parent: ViewGroup) -> VB,
    onCreateItemViewModel: (binding: VB, data: Data?) -> VM,
    onSetVariable: (binding: VB, viewModel: VM) -> Boolean = { _, _ -> false },
    diffCallback: DiffUtil.ItemCallback<Data>,
) : BindingPagingDataAdapter<VB, VM, Data>(
    DefaultItemViewDataBindingItemType(onCreateViewDataBinding, onCreateItemViewModel, onSetVariable),
    diffCallback
)
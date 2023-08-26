package com.pp.ui.adapter

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil

abstract class BindingPagingDataAdapter<
        VB : ViewDataBinding,
        VM : Any?,
        Data : Any,
        ViewBindingItemType : ViewDataBindingItemType<VB, VM, Data>,
        >(
    diffCallback: DiffUtil.ItemCallback<Data>,
) : PagingDataAdapter<Data, BindingItemViewHolder<VB, VM, Data>>(diffCallback) {

    private val delegate = RecyclerViewAdapterDelegate<VB, VM, Data, ViewBindingItemType>()
    abstract fun createViewBindingItemType(viewType: Int): ViewBindingItemType
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BindingItemViewHolder<VB, VM, Data> {
        val viewBindingItemType = createViewBindingItemType(viewType)
        return delegate.onCreateViewHolder(parent, viewBindingItemType)
    }

    override fun onBindViewHolder(holder: BindingItemViewHolder<VB, VM, Data>, position: Int) {
        val itemData = getItem(position)
        delegate.onBindViewHolder(holder, itemData,position)
    }

    override fun onViewAttachedToWindow(holder: BindingItemViewHolder<VB, VM, Data>) {
        super.onViewAttachedToWindow(holder)
        delegate.onViewAttachedToWindow(holder)
    }

    class DefaultBindingPagingDataAdapter<VB : ViewDataBinding, VM : Any?, Data : Any>(
        private val onCreateViewDataBinding: (parent: ViewGroup) -> VB,
        private val onBindItemViewModel: (binding: VB, data: Data?, position: Int, cachedItemViewModel: VM?) -> VM,
        private val onSetVariable: (binding: VB, viewModel: VM) -> Boolean = { _, _ -> false },
        private val getItemType: () -> Int = { 0 },
        diffCallback: DiffUtil.ItemCallback<Data>,
    ) : BindingPagingDataAdapter<VB, VM, Data, ViewDataBindingItemType<VB, VM, Data>>(
        diffCallback
    ) {
        override fun createViewBindingItemType(viewType: Int): ViewDataBindingItemType<VB, VM, Data> {
            return DefaultViewDataBindingItemType(
                onCreateViewDataBinding,
                onBindItemViewModel,
                onSetVariable,
                getItemType
            )
        }
    }


}
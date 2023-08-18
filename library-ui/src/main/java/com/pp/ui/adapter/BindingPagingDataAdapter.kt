package com.pp.ui.adapter

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil

open class BindingPagingDataAdapter<VB : ViewDataBinding, VM : Any?, Data : Any>(
    private val viewBindingItem: ViewDataBindingItemType<VB, VM, Data>,
    diffCallback: DiffUtil.ItemCallback<Data>,
) : PagingDataAdapter<Data, BindingItemViewHolder<VB>>(diffCallback) {

    override fun onBindViewHolder(holder: BindingItemViewHolder<VB>, position: Int) {
        val itemData = getItem(position)
        val itemViewModel = viewBindingItem.createItemViewModel(holder.binding, itemData)

        viewBindingItem.setVariable(holder.binding, itemViewModel)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingItemViewHolder<VB> {
        val binding = viewBindingItem.createViewDataBinding(parent)
        return BindingItemViewHolder(binding)
    }

    override fun onViewAttachedToWindow(holder: BindingItemViewHolder<VB>) {
        super.onViewAttachedToWindow(holder)
        viewBindingItem.onViewAttachedToWindow(holder.binding)
    }


}
package com.pp.ui.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil

class MultiBindingPagingDataAdapter<
        VB : ViewDataBinding,
        VM : Any?,
        Data : Any>
    (diffCallback: DiffUtil.ItemCallback<Data>) :
    BindingPagingDataAdapter<VB, VM, Data, ViewDataBindingItemType<VB, VM, Data>>(
        diffCallback
    ) {

    private val delegate = MultiAdapterDelegate<VB,VM,Data>()

    fun addBindingItem(item: ViewDataBindingItemType<VB, VM, Data>) {
        delegate.addBindingItem(item)
    }

    override fun getItemViewType(position: Int): Int {
        val item = peek(position)
        return delegate.getItemViewType(item)
    }

    override fun createViewBindingItemType(viewType: Int): ViewDataBindingItemType<VB, VM, Data> {
        return delegate.createViewBindingItemType(viewType)
    }

}
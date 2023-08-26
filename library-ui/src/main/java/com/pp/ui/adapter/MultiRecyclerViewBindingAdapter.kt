package com.pp.ui.adapter

import androidx.databinding.ViewDataBinding

class MultiRecyclerViewBindingAdapter<
        VB : ViewDataBinding,
        VM : Any?,
        Data : Any,
        > :
    RecyclerViewBindingAdapter<VB, VM, Data, ViewDataBindingItemType<VB, VM, Data>>() {

    private val delegate = MultiAdapterDelegate<VB, VM, Data>()

    fun addBindingItem(item: ViewDataBindingItemType<VB, VM, Data>) {
        delegate.addBindingItem(item)
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return delegate.getItemViewType(item)
    }

    override fun createViewBindingItemType(viewType: Int): ViewDataBindingItemType<VB, VM, Data> {
        return delegate.createViewBindingItemType(viewType)
    }

}
package com.pp.ui.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil

class MultiBindingPagingDataAdapter
    (getItemViewType: (data: Any?) -> Int, diffCallback: DiffUtil.ItemCallback<Any>) :
    BindingPagingDataAdapter<ViewDataBinding, Any?, Any, ViewDataBindingItemType<ViewDataBinding, Any?, Any>>(
        diffCallback
    ) {

    private val delegate = MultiAdapterDelegate(getItemViewType)

    fun addBindingItem(item: ViewDataBindingItemType<ViewDataBinding, Any?, Any>) {
        delegate.addBindingItem(item)
    }

    override fun getItemViewType(position: Int): Int {
        val item = peek(position)
        return delegate.getItemViewType(item)
    }

    override fun createViewBindingItemType(viewType: Int): ViewDataBindingItemType<ViewDataBinding, Any?, Any> {
        return delegate.createViewBindingItemType(viewType)
    }

}
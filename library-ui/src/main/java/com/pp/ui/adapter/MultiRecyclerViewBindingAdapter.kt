package com.pp.ui.adapter

import androidx.databinding.ViewDataBinding

class MultiRecyclerViewBindingAdapter(getItemViewType: (data: Any?) -> Int) :
    RecyclerViewBindingAdapter<ViewDataBinding, Any?, Any, ViewDataBindingItemType<ViewDataBinding, Any?, Any>>() {

    private val delegate = MultiAdapterDelegate(getItemViewType)

    fun hasItemType(itemType: Int): Boolean {
        return delegate.hasItemType(itemType)
    }

    fun addBindingItem(item: ViewDataBindingItemType<ViewDataBinding, Any?, Any>) {
        delegate.addBindingItem(item)
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return delegate.getItemViewType(item)
    }

    override fun createViewBindingItemType(viewType: Int): ViewDataBindingItemType<ViewDataBinding, Any?, Any> {
        return delegate.createViewBindingItemType(viewType)
    }

}
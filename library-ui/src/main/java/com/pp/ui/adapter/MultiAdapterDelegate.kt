package com.pp.ui.adapter

import androidx.databinding.ViewDataBinding

class MultiAdapterDelegate<
        VB : ViewDataBinding,
        VM : Any?,
        Data : Any,
        > {

    private val mViewTypeAdapterMap by lazy { mutableMapOf<Int, ViewDataBindingItemType<VB, VM, Data>>() }

    fun addBindingItem(item: ViewDataBindingItemType<VB, VM, Data>) {
        val type = item.getItemType()

        if (mViewTypeAdapterMap.containsKey(type)) {
            throw RuntimeException("The item type already exists: {type: ${type}}")
        }
        mViewTypeAdapterMap[type] = item
    }
    
    private fun getViewDataBindingItemType(viewType: Int): ViewDataBindingItemType<VB, VM, Data>? {
        return mViewTypeAdapterMap[viewType].also {
            if (null == it) {
                throw RuntimeException("you should call addBindingItem() for viewType: $viewType at first")
            }
        }
    }

    fun getItemViewType(data: Data?): Int {
        // 查找 item type
        for (entry in mViewTypeAdapterMap) {
            if (entry.key == entry.value.getItemType(data)) {
                return entry.key
            }
        }

        throw RuntimeException("ViewBindingItem not found: { item: ${data}}")
    }

    fun createViewBindingItemType(viewType: Int): ViewDataBindingItemType<VB, VM, Data> {
        return getViewDataBindingItemType(viewType)!!
    }
}
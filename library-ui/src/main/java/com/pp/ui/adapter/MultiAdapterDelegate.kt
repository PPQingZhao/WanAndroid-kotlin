package com.pp.ui.adapter

import androidx.databinding.ViewDataBinding

class MultiAdapterDelegate(private val getItemViewType: (data: Any?) -> Int) {

    private val mViewTypeAdapterMap by lazy { mutableMapOf<Int, ViewDataBindingItemType<ViewDataBinding, Any?, Any>>() }

    fun hasItemType(itemType: Int): Boolean {
        return mViewTypeAdapterMap.containsKey(itemType)
    }

    fun addBindingItem(item: ViewDataBindingItemType<ViewDataBinding, Any?, Any>) {
        val type = item.getItemType()

        if (mViewTypeAdapterMap.containsKey(type)) {
            throw RuntimeException("The item type already exists: {type: ${type}}")
        }
        mViewTypeAdapterMap[type] = item
    }

    private fun getViewDataBindingItemType(viewType: Int): ViewDataBindingItemType<ViewDataBinding, Any?, Any> {
        return mViewTypeAdapterMap[viewType].also {
            if (null == it) {
                throw RuntimeException("you should call addBindingItem() for viewType: $viewType at first")
            }
        }!!
    }

    fun getItemViewType(data: Any?): Int {
        val itemType = getItemViewType.invoke(data)
        if (mViewTypeAdapterMap[itemType] == null) {
            throw RuntimeException("ViewBindingItem not found: { item: ${data}}")
        }
        return itemType
    }

    fun createViewBindingItemType(viewType: Int): ViewDataBindingItemType<ViewDataBinding, Any?, Any> {
        return getViewDataBindingItemType(viewType)
    }
}
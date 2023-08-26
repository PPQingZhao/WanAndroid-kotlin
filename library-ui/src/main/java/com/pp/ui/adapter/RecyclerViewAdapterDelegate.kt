package com.pp.ui.adapter

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

class RecyclerViewAdapterDelegate<
        VB : ViewDataBinding,
        VM : Any?,
        Data : Any,
        ViewBindingItemType : ViewDataBindingItemType<VB, VM, Data>,
        > {

    private val mItemModelCaches by lazy { mutableMapOf<Int, VM>() }
    fun onCreateViewHolder(
        parent: ViewGroup,
        viewBindingItemType: ViewBindingItemType,
    ): BindingItemViewHolder<VB, VM, Data> {
        val binding = viewBindingItemType.createViewDataBinding(parent)
        return BindingItemViewHolder(viewBindingItemType, binding)
    }

    fun onBindViewHolder(holder: BindingItemViewHolder<VB, VM, Data>, data: Data?, position: Int) {
        holder.viewDataBindingItemType.let {
            var itemViewModel = mItemModelCaches[position]
            itemViewModel = it.bindItemViewModel(holder.binding, data, position, itemViewModel)
            mItemModelCaches[position] = itemViewModel

        }
    }

    fun onViewAttachedToWindow(holder: BindingItemViewHolder<VB, VM, Data>) {
        holder.viewDataBindingItemType.onViewAttachedToWindow(holder.binding)
    }

}
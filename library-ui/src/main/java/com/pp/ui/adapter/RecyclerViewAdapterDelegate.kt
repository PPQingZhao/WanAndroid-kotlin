package com.pp.ui.adapter

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

class RecyclerViewAdapterDelegate<
        VB : ViewDataBinding,
        VM : Any?,
        Data : Any,
        ViewBindingItemType : ViewDataBindingItemType<VB, VM, Data>,
        > {

    fun onCreateViewHolder(
        parent: ViewGroup,
        viewBindingItemType: ViewBindingItemType,
    ): BindingItemViewHolder<VB, VM, Data> {
        val binding = viewBindingItemType.createViewDataBinding(parent)
        return BindingItemViewHolder(viewBindingItemType, binding)
    }

    fun onBindViewHolder(holder: BindingItemViewHolder<VB, VM, Data>, data: Data?) {
        holder.viewDataBindingItemType.let {
            val itemViewModel = it.createItemViewModel(holder.binding, data)
            it.setVariable(holder.binding, itemViewModel)

        }
    }

    fun onViewAttachedToWindow(holder: BindingItemViewHolder<VB, VM, Data>) {
        holder.viewDataBindingItemType.onViewAttachedToWindow(holder.binding)
    }

}
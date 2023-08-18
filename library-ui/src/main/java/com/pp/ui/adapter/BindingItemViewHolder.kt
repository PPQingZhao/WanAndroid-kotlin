package com.pp.ui.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class BindingItemViewHolder<
        VB : ViewDataBinding,
        VM : Any?,
        Data : Any,
        >(
    val viewDataBindingItemType: ViewDataBindingItemType<VB, VM, Data>,
    val binding: VB,
) :
    RecyclerView.ViewHolder(binding.root)

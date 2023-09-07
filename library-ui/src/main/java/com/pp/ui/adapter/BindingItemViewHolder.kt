package com.pp.ui.adapter

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewTreeLifecycleOwner
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

class BindingItemViewHolder2(val bind: ViewDataBinding) : RecyclerView.ViewHolder(bind.root)
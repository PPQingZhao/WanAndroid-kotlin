package com.pp.ui.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class ViewDataBindingItemViewHolder(bind: ViewDataBinding) :
    BindingItemViewHolder<ViewDataBinding>(bind)


open class BindingItemViewHolder<VB : ViewDataBinding>(val bind: VB) :
    RecyclerView.ViewHolder(bind.root)
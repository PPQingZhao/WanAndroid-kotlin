package com.pp.ui.adapter

import androidx.databinding.ViewDataBinding

class MultiItemViewHolder<Data : Any?>(
    val viewBindingItem: ViewBindingItem<Data>,
    binding: ViewDataBinding
) :
    BindingHolder<ViewDataBinding>(binding)
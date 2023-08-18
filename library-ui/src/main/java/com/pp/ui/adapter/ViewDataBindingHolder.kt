package com.pp.ui.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class ViewDataBindingHolder<VB : ViewDataBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)
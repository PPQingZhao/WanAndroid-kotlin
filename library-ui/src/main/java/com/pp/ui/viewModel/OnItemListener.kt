package com.pp.ui.viewModel

import android.view.View

interface OnItemListener<Item> {
    fun onItemClick(view: View, item: Item): Boolean
}
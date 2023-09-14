package com.pp.ui.utils

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.pp.ui.adapter.BindingPagingDataAdapter
import com.pp.ui.adapter.attachRecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun <Data : Any> RecyclerView.setPagingAdapter(
    lifecycleScope: LifecycleCoroutineScope,
    pageData: Flow<PagingData<Data>>? = null,
    pagingAdapter: BindingPagingDataAdapter<Data>,
    layoutManager: LayoutManager,
) {
    pagingAdapter.attachRecyclerView(this, layoutManager)
    lifecycleScope.launch(Dispatchers.IO) {
        pageData?.collect {
            pagingAdapter.setPagingData(this, it)
        }
    }
}
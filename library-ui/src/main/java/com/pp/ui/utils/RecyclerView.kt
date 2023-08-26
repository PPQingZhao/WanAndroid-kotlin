package com.pp.ui.utils

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun <Data : Any> RecyclerView.setPagingAdapter(
    lifecycleOwner: LifecycleOwner,
    lifecycleScope: LifecycleCoroutineScope,
    pageData: Flow<PagingData<Data>>,
    pagingAdapter: PagingDataAdapter<Data, out RecyclerView.ViewHolder>,
) {
    adapter = pagingAdapter
    lifecycleScope.launch(Dispatchers.IO) {
        pageData.collect {
            pagingAdapter.submitData(lifecycleOwner.lifecycle, it)
        }
    }
}
package com.pp.ui.utils

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.pp.ui.adapter.BindingPagingDataAdapter
import com.pp.ui.adapter.attachRecyclerView
import com.pp.ui.adapter.attachRefreshView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun <Data : Any> RecyclerView.setPagingAdapter(
    lifecycleScope: LifecycleCoroutineScope,
    pageData: Flow<PagingData<Data>>? = null,
    pagingAdapter: BindingPagingDataAdapter<Data>,
    layoutManager: LayoutManager,
    refreshLayout: SwipeRefreshLayout? = null,
    onReFreshChildScrollUp: () -> Boolean = { false },
) {
    refreshLayout?.setOnChildScrollUpCallback { parent, child ->
        onReFreshChildScrollUp.invoke() || canScrollVertically(-1)
    }

    pagingAdapter.attachRecyclerView(this, layoutManager)
    refreshLayout?.let {
        pagingAdapter.attachRefreshView(it)
    }

    lifecycleScope.launch(Dispatchers.IO) {
        pageData?.collect {
            pagingAdapter.setPagingData(this, it)
        }
    }
}
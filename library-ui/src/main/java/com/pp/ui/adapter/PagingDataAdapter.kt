package com.pp.ui.adapter

import android.view.View
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.pp.ui.viewModel.ItemDataViewModel


fun <Data : Any, VM : ItemDataViewModel<Data>> createItemDataBindingPagingDataAdapter(
    getItemLayoutRes: (data: VM?) -> Int,
    areContentsTheSame: (oldItem: VM, newItem: VM) -> Boolean,
    areItemsTheSame: (oldItem: VM, newItem: VM) -> Boolean,
): BindingPagingDataAdapter<VM> {
    return BindingPagingDataAdapter(
        getItemLayoutRes = getItemLayoutRes,
        diffCallback = getItemDataDifferCallback(
            areContentsTheSame = areContentsTheSame,
            areItemsTheSame = areItemsTheSame
        )
    )
}

fun <Data : Any, Item : ItemDataViewModel<Data>> getItemDataDifferCallback(
    areItemsTheSame: (oldItem: Item, newItem: Item) -> Boolean,
    areContentsTheSame: (oldItem: Item, newItem: Item) -> Boolean,
) = object : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(
        oldItem: Item,
        newItem: Item,
    ): Boolean = areItemsTheSame.invoke(oldItem, newItem)

    override fun areContentsTheSame(
        oldItem: Item,
        newItem: Item,
    ): Boolean = areContentsTheSame.invoke(oldItem, newItem)
}

fun <VH : RecyclerView.ViewHolder, Adapter : PagingDataAdapter<*, VH>> Adapter.attachRecyclerView(
    recyclerView: RecyclerView,
    layoutManager: RecyclerView.LayoutManager,
    getStateViewType: (loadState: LoadState) -> Int = { 0 },
    withLoadMore: Boolean = true,
    onErrorListener: View.OnClickListener? = View.OnClickListener {
        // 默认重试
        retry()
    },
) {
    recyclerView.layoutManager = layoutManager
    recyclerView.adapter = if (withLoadMore) withLoadStateFooter(
        DefaultLoadMoreStateAdapter(
            getStateViewType = getStateViewType, onErrorListener = onErrorListener
        )
    ) else this
}

fun <VH : RecyclerView.ViewHolder, Adapter : PagingDataAdapter<*, VH>> Adapter.attachRefreshView(
    refreshView: SwipeRefreshLayout,
) {

    refreshView.setOnRefreshListener {
        this.refresh()
    }
    addLoadStateListener {
        refreshView.isRefreshing = itemCount > 0 && it.refresh is LoadState.Loading
    }
}
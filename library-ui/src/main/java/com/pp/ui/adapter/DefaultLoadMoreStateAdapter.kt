package com.pp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.view.doOnAttach
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.pp.theme.AppDynamicTheme
import com.pp.theme.ViewTreeAppThemeViewModel
import com.pp.ui.databinding.ItemDefaultLoadmoreBinding

class DefaultLoadMoreStateAdapter(
    private val getStateViewType: (loadState: LoadState) -> Int = { 0 },
    private val onErrorListener: OnClickListener? = null,
) :
    LoadStateAdapter<BindingItemViewHolder<ItemDefaultLoadmoreBinding>>() {

    override fun getStateViewType(loadState: LoadState): Int {
        return getStateViewType.invoke(loadState)
    }

    override fun onBindViewHolder(
        holder: BindingItemViewHolder<ItemDefaultLoadmoreBinding>,
        loadState: LoadState,
    ) {

//        Log.e("DefaultLoadStateAdapter", loadState.toString())
        holder.bind.loading.visibility =
            if (loadState is LoadState.Loading) View.VISIBLE else View.GONE

        holder.bind.loadError.visibility =
            if (loadState is LoadState.Error) View.VISIBLE else View.GONE
        // 错误
        holder.bind.loadError.setOnClickListener(onErrorListener)

        holder.bind.loadDataEmpty.visibility =
            if (loadState is LoadState.NotLoading && loadState.endOfPaginationReached) View.VISIBLE else View.GONE

        holder.itemView.doOnAttach {
            ViewTreeAppThemeViewModel.get<AppDynamicTheme>(it)?.also {
                holder.bind.theme = it
            }
            ViewTreeLifecycleOwner.get(it)?.also { owner ->
                holder.bind.lifecycleOwner = owner
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState,
    ): BindingItemViewHolder<ItemDefaultLoadmoreBinding> {
//        Log.e("DefaultLoadStateAdapter", loadState.toString())
        return BindingItemViewHolder(
            ItemDefaultLoadmoreBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
//        Log.e("DefaultLoadStateAdapter", "displayLoadStateAsItem:   ${loadState.toString()}")
        // 父类默认实现: loading 或者 err 显示
        return super.displayLoadStateAsItem(loadState)
                // no data: not loading 并且 已到底部 (endOfPaginationReached) 显示
                || (loadState is LoadState.NotLoading && loadState.endOfPaginationReached)
    }


}
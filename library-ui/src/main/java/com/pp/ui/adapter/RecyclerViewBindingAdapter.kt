package com.pp.ui.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerViewBindingAdapter<VB : ViewDataBinding, VM : Any?, T : Any>(
    private val viewBindingItem: ViewDataBindingItemType<VB, VM, T>,
) :
    RecyclerView.Adapter<BindingItemViewHolder<VB>>() {

    private val dataList by lazy { mutableListOf<T>() }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataList(list: List<T>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }


    protected fun getItem(position: Int): T? {
        return if (position >= 0 && position < dataList.size) {
            dataList[position]
        } else {
            null
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: BindingItemViewHolder<VB>, position: Int) {
        val itemData = getItem(position)
        val itemViewModel = viewBindingItem.createItemViewModel(holder.binding, itemData)

        viewBindingItem.setVariable(holder.binding, itemViewModel)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingItemViewHolder<VB> {
        val binding = viewBindingItem.createViewDataBinding(parent)
        return BindingItemViewHolder(binding)
    }

    override fun onViewAttachedToWindow(holder: BindingItemViewHolder<VB>) {
        super.onViewAttachedToWindow(holder)
        viewBindingItem.onViewAttachedToWindow(holder.binding)
    }

    class DefaultRecyclerViewBindingAdapter<VB : ViewDataBinding, VM : Any?, Data : Any>(
        onCreateViewDataBinding: (parent: ViewGroup) -> VB,
        onCreateItemViewModel: (binding: VB, data: Data?) -> VM,
        onSetVariable: (binding: VB, viewModel: VM) -> Boolean = { _, _ -> false },
    ) : RecyclerViewBindingAdapter<VB, VM, Data>(
        DefaultItemViewDataBindingItemType(
            onCreateViewDataBinding,
            onCreateItemViewModel,
            onSetVariable
        ),
    )

}

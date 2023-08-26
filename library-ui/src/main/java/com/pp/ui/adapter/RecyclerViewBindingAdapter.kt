package com.pp.ui.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerViewBindingAdapter<
        VB : ViewDataBinding,
        VM : Any?,
        Data : Any,
        ViewBindingItemType : ViewDataBindingItemType<VB, VM, Data>,
        > :
    RecyclerView.Adapter<BindingItemViewHolder<VB, VM, Data>>() {

    private val delegate = RecyclerViewAdapterDelegate<VB, VM, Data, ViewBindingItemType>()
    private val dataList by lazy { mutableListOf<Data>() }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataList(list: List<Data>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    fun getData(): List<Data> {
        return dataList
    }

    protected fun getItem(position: Int): Data? {
        return if (position >= 0 && position < dataList.size) {
            dataList[position]
        } else {
            null
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    abstract fun createViewBindingItemType(viewType: Int): ViewBindingItemType
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BindingItemViewHolder<VB, VM, Data> {
        val viewBindingItemType = createViewBindingItemType(viewType)
        return delegate.onCreateViewHolder(parent, viewBindingItemType)
    }

    override fun onBindViewHolder(holder: BindingItemViewHolder<VB, VM, Data>, position: Int) {
        val itemData = getItem(position)
        delegate.onBindViewHolder(holder, itemData, position)
    }

    override fun onViewAttachedToWindow(holder: BindingItemViewHolder<VB, VM, Data>) {
        super.onViewAttachedToWindow(holder)
        delegate.onViewAttachedToWindow(holder)
    }


    class DefaultRecyclerViewBindingAdapter<VB : ViewDataBinding, VM : Any?, Data : Any>(
        private val onCreateBinding: (parent: ViewGroup) -> VB,
        private val onBindItemModel: (binding: VB, data: Data?, position: Int, cachedItemViewModel: VM?) -> VM,
        private val onSetVariable: (binding: VB, viewModel: VM) -> Boolean = { _, _ -> false },
        private val getItemType: () -> Int = { 0 },
    ) : RecyclerViewBindingAdapter<VB, VM, Data, ViewDataBindingItemType<VB, VM, Data>>() {
        override fun createViewBindingItemType(viewType: Int): ViewDataBindingItemType<VB, VM, Data> {
            return DefaultViewDataBindingItemType(
                onCreateBinding,
                onBindItemModel,
                onSetVariable,
                getItemType
            )
        }
    }

}

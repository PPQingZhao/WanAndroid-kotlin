package com.pp.ui.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.pp.ui.BR

abstract class ItemBinder<VB : ViewDataBinding, Data : Any> :
    RecyclerView.AdapterDataObserver() {

    abstract fun getItemViewBindingClazz(): Class<VB>
    abstract fun getItemDataClazz(): Class<Data>
    abstract fun bindItem(binding: VB, position: Int, data: Data?)

}

abstract class ItemViewModelBinder<VB : ViewDataBinding, Data : Any, VM : Any>
    (private val onBindViewModel: (binding: VB, viewModel: VM?, position: Int, data: Data?) -> Boolean = { _, _, _, _ -> false }) :
    ItemBinder<VB, Data>() {

    private val mItemModelCaches by lazy { mutableMapOf<Int, VM?>() }
    abstract fun getItemViewModel(data: Data?): VM?

    override fun bindItem(binding: VB, position: Int, data: Data?) {
        var itemViewModel = mItemModelCaches[position]
        if (null == itemViewModel) {
            itemViewModel = getItemViewModel(data)
            mItemModelCaches[position] = itemViewModel
        }
        onBindViewModel(binding, itemViewModel, position, data)
    }

    open fun onBindViewModel(
        binding: VB,
        viewModel: VM?,
        position: Int,
        data: Data?,
    ) {

        if (onBindViewModel.invoke(binding, viewModel, position, data)) {
            return
        }

        binding.setVariable(BR.viewModel, viewModel)
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        val tempCacheMap = mItemModelCaches.toMap()
        mItemModelCaches.clear()

        tempCacheMap.filter {
            // 过滤 remove range对应缓存的itemViewModel
            val itemPosition = it.key
            itemPosition < positionStart || itemPosition >= positionStart + itemCount
        }.onEach { entry ->
            var itemPosition = entry.key
            // 将大于删除位置缓存的itemViewModel位置前移
            if (itemPosition > positionStart) {
                itemPosition = itemPosition - itemCount
            }
            mItemModelCaches[itemPosition] = entry.value
        }
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        val tempCacheMap = mItemModelCaches.toMap()
        mItemModelCaches.clear()

        tempCacheMap.onEach {
            var itemPosition = it.key
            val vewModelValue: VM? = it.value
            // 将缓存的itemViewModel位置后移
            if (itemPosition >= positionStart) {
                itemPosition = itemPosition + itemCount
            }
            mItemModelCaches[itemPosition] = vewModelValue
        }
    }

}

inline fun <reified VB : ViewDataBinding, reified Data : Any, VM : Any> createItemViewModelBinder(
    crossinline getItemViewModel: (data: Data?) -> VM?,
    noinline onBindViewModel: (binding: VB, viewModel: VM?, position: Int, data: Data?) -> Boolean = { _, _, _, _ -> false },
) = object : ItemViewModelBinder<VB, Data, VM>(onBindViewModel = onBindViewModel) {
    override fun getItemViewModel(data: Data?): VM? {
        return getItemViewModel.invoke(data)
    }

    override fun getItemViewBindingClazz(): Class<VB> {
        return VB::class.java
    }

    override fun getItemDataClazz(): Class<Data> {
        return Data::class.java
    }

}
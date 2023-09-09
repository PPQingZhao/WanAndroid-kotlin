package com.pp.ui.adapter

import androidx.databinding.ViewDataBinding
import com.pp.ui.BR

interface ItemViewModelBinder<VB : ViewDataBinding, Data : Any> {

    fun getViewDataBindingClazz(): Class<VB>
    fun getDataClazz(): Class<Data>
    fun bindViewModel(binding: VB, data: Data?, position: Int)
    fun onRemoveItem(data: List<Data?>, startPosition: Int, itemCount: Int) {}
    fun onAddItem(data: List<Data?>, startPosition: Int, itemCount: Int) {}
}

abstract class DefaultItemViewModelBinder<VB : ViewDataBinding, Data : Any, VM : Any>
    (private val onBindViewModel: (binding: VB, data: Data?, viewModel: VM?, posiion: Int) -> Boolean = { _, _, _, _ -> false }) :
    ItemViewModelBinder<VB, Data> {

    private val mItemModelCaches by lazy { mutableMapOf<Int, VM?>() }
    abstract fun getItemViewModel(data: Data?): VM?

    override fun bindViewModel(binding: VB, data: Data?, position: Int) {
        var itemViewModel = mItemModelCaches[position]
        if (null == itemViewModel) {
            itemViewModel = getItemViewModel(data)
            mItemModelCaches[position] = itemViewModel
        }
        onBindViewModel(binding, data, itemViewModel, position)
    }

    open fun onBindViewModel(binding: VB, data: Data?, viewModel: VM?, posiion: Int) {

        if (onBindViewModel.invoke(binding, data, viewModel, posiion)) {
            return
        }

        binding.setVariable(BR.viewModel, viewModel)
    }

    override fun onRemoveItem(data: List<Data?>, startPosition: Int, itemCount: Int) {
        // 删除对应缓存的itemViewModel
        for (pos in data.indices) {
            mItemModelCaches.remove(startPosition + pos)
        }
        val tempCacheMap = mItemModelCaches.toMap()
        mItemModelCaches.clear()
        var itemPosition = 0
        val oldItemCount = itemCount + data.size
        for (entry in tempCacheMap) {

            itemPosition = entry.key
            // 无效 item
            if (itemPosition < 0 || itemPosition > oldItemCount - 1) {
                continue
            }

            // 将缓存的itemViewModel位置前移
            if (itemPosition > startPosition) {
                itemPosition = itemPosition - data.size
                mItemModelCaches[itemPosition] = entry.value
            }
        }
    }

    override fun onAddItem(data: List<Data?>, startPosition: Int, itemCount: Int) {
        val tempCacheMap = mItemModelCaches.toMap()
        mItemModelCaches.clear()
        var itemPosition = 0
        var vewModelValue: VM? = null
        val oldItemCount = itemCount + data.size
        // 添加的索引区间
        val addIndexRange = startPosition..startPosition + data.size - 1
        for (entry in tempCacheMap) {

            itemPosition = entry.key
            // 无效 item
            if (itemPosition < 0 || itemPosition > oldItemCount - 1) {
                continue
            }

            // 将缓存的itemViewModel位置后移
            if (itemPosition >= addIndexRange.first) {
                vewModelValue = entry.value
                itemPosition = itemPosition + addIndexRange.count()
                mItemModelCaches[itemPosition] = vewModelValue
            }

        }
    }
}
package com.pp.ui.adapter

import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.pp.ui.BR

abstract class ItemBinder<VB : ViewDataBinding, Data : Any> :
    RecyclerView.AdapterDataObserver() {

    abstract fun getItemViewBindingClazz(): Class<VB>
    abstract fun getItemDataClazz(): Class<Data>
    abstract fun bindItem(binding: VB, data: Data?, position: Int)
}

abstract class ItemViewModelBinder<VB : ViewDataBinding, Data : Any, VM : Any>
    (private val onBindViewModel: (binding: VB, data: Data?, viewModel: VM?, posiion: Int) -> Boolean = { _, _, _, _ -> false }) :
    ItemBinder<VB, Data>() {

    private val mItemModelCaches by lazy { mutableMapOf<Int, VM?>() }
    abstract fun getItemViewModel(data: Data?): VM?

    override fun bindItem(binding: VB, data: Data?, position: Int) {
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

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        Log.e("TAG", "onItemRangeMoved")
    }

}
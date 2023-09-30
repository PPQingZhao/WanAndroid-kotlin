package com.pp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.view.doOnAttach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.paging.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pp.common.paging.onePager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BindingPagingDataAdapter<Data : Any>(
    @SuppressLint("SupportAnnotationUsage") @LayoutRes
    private val getItemLayoutRes: (data: Data?) -> Int,
    diffCallback: DiffUtil.ItemCallback<Data> = emptyDifferCallback(),
) : PagingDataAdapter<Data, ViewDataBindingItemViewHolder>(diffCallback) {

    companion object {
        fun <Data : Any> emptyDifferCallback() = object : DiffUtil.ItemCallback<Data>() {
            override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var mInflater: LayoutInflater? = null
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        mInflater = LayoutInflater.from(recyclerView.context)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        mInflater = null
    }

    private val mBindViewModelList = mutableListOf<ItemBinder<ViewDataBinding, Data>>()

    fun addItemViewModelBinder(itemViewModelBinder: ItemBinder<out ViewDataBinding, Data>) {
        registerAdapterDataObserver(itemViewModelBinder)
        mBindViewModelList.add(itemViewModelBinder as ItemBinder<ViewDataBinding, Data>)
    }

    private fun getItemViewModelBinder(
        bind: ViewDataBinding?,
        data: Data?,
    ): ItemBinder<ViewDataBinding, Data> {

        for (binder in mBindViewModelList) {

            if (null != data && !binder.getItemDataClazz().isAssignableFrom(data::class.java)) {
                continue
            }

            val viewDataBindingClazz = binder.getItemViewBindingClazz()
            if (null != bind && !viewDataBindingClazz.isAssignableFrom(bind::class.java)) {
                continue
            }
            return binder
        }
        throw RuntimeException("No BindViewModel for {bind:${bind},data:${data}}")
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, @LayoutRes layoutId: Int,
    ): ViewDataBindingItemViewHolder {
        return ViewDataBindingItemViewHolder(
            DataBindingUtil.inflate<ViewDataBinding>(
                mInflater!!, layoutId, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewDataBindingItemViewHolder, position: Int) {
        val itemData = getItem(position)
        getItemViewModelBinder(holder.bind, itemData).apply {
            bindItem(holder.bind, position, itemData)
        }
        holder.itemView.doOnAttach {
            ViewTreeLifecycleOwner.get(it)?.also { owner ->
                holder.bind.lifecycleOwner = owner
            }
        }

        holder.bind.executePendingBindings()
    }

    @LayoutRes
    override fun getItemViewType(position: Int): Int {
        val data = getItem(position)
        return getItemLayoutRes.invoke(data)
    }

    private var mScope: CoroutineScope? = null
        get() = checkNotNull(field) { "you must call 'setPagingData()' at first." }

    private var mPagingData: PagingData<Data>? = null
        get() = checkNotNull(field) { "you must call 'setPagingData()' at first." }

    fun setPagingData(scope: CoroutineScope, pagingData: PagingData<Data>) {
        mScope = scope
        mPagingData = pagingData
        submitPageData()
    }

    private fun submitPageData() {
        mScope!!.launch {
            submitData(mPagingData!!)
        }
    }

    fun remove(item: Data) {
        filterItem { item != it }
    }

    fun filterItem(predicate: suspend (Data) -> Boolean) {
        mPagingData = mPagingData!!.filter(predicate)
        submitPageData()
    }

    fun update(notify: Boolean = false, block: (data: Data?) -> Boolean) {
        for (pos in 0 until itemCount) {
            if (block.invoke(getItem(pos))) {
                if (notify) {
                    notifyItemChanged(pos)
                }
                break
            }
        }
    }

    fun clear() {
        filterItem { false }
    }

    fun setData(scope: CoroutineScope, dataList: List<Data>) {
        scope.launch {
            onePager(getPageData = { dataList },
                getPageValue = {
                    it ?: emptyList()
                }).flow.cachedIn(scope)
                .collectLatest {
                    setPagingData(scope, it)
                }
        }
    }
}


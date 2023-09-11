package com.pp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewBindingAdapter2<Data : Any>(
    @SuppressLint("SupportAnnotationUsage") @LayoutRes
    private val getItemLayoutRes: (data: Data?) -> Int,
) : RecyclerView.Adapter<BindingItemViewHolder>() {


    private var mInflater: LayoutInflater? = null
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        mInflater = LayoutInflater.from(recyclerView.context)
    }

    private val mBindViewModelList = mutableListOf<ItemViewModelBinder<ViewDataBinding, Data>>()

    fun addItemViewModelBinder(itemViewModelBinder: ItemViewModelBinder<out ViewDataBinding, Data>) {
        mBindViewModelList.add(itemViewModelBinder as ItemViewModelBinder<ViewDataBinding, Data>)
    }

    private fun getBindViewModel(
        bind: ViewDataBinding?,
        data: Data?,
    ): ItemViewModelBinder<ViewDataBinding, Data> {
        val dataClazz = if (data == null) {
            null
        } else {
            data::class.java
        }
        for (binder in mBindViewModelList) {

            if (binder.getDataClazz() != dataClazz) {
                continue
            }

            val viewDataBindingClazz = binder.getViewDataBindingClazz()
            if (null != bind && !viewDataBindingClazz.isAssignableFrom(bind.javaClass)) {
                continue
            }
            return binder
        }
        throw RuntimeException("No BindViewModel for {bind:${bind},data:${dataClazz}}")
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        @LayoutRes layoutId: Int,
    ): BindingItemViewHolder {
        val bind = DataBindingUtil.inflate<ViewDataBinding>(mInflater!!, layoutId, parent, false)
            .also { binding ->
                binding.root.addOnAttachStateChangeListener(object :
                    View.OnAttachStateChangeListener {
                    override fun onViewAttachedToWindow(v: View) {
                        ViewTreeLifecycleOwner.get(v)?.also {
                            binding.lifecycleOwner = it
                        }
                    }

                    override fun onViewDetachedFromWindow(v: View) {}
                })
            }
        return BindingItemViewHolder(bind)
    }

    override fun onBindViewHolder(holder: BindingItemViewHolder, position: Int) {
        val itemData = getItem(position)
        getBindViewModel(holder.bind, itemData).apply {
            bindViewModel(holder.bind, itemData, position)
        }
        holder.bind.executePendingBindings()
    }

    @LayoutRes
    override fun getItemViewType(position: Int): Int {
        val data = getItem(position)
        return getItemLayoutRes.invoke(data)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

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


}
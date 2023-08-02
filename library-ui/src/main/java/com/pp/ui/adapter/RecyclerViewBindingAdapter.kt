package com.pp.ui.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.pp.theme.ViewTreeAppThemeViewModel

abstract class RecyclerViewBindingAdapter<VB : ViewDataBinding, VM : Any, T : Any> :
    RecyclerView.Adapter<BindingHolder<VB>>() {

    private val dataList by lazy { mutableListOf<T>() }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataList(list: List<T>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    private val bindingHelper: AdapterBindingHelper<VB, VM, T> by lazy {
        object : AdapterBindingHelper<VB, VM, T>() {
            override fun createViewModel(binding: VB, item: T?, cacheItemViewModel: VM?): VM? {
                return this@RecyclerViewBindingAdapter.createViewModel(
                    binding,
                    item,
                    cacheItemViewModel
                )
            }

            override fun onCreateBinding(parent: ViewGroup, viewType: Int): VB {
                return this@RecyclerViewBindingAdapter.onCreateBinding(parent, viewType)
            }

            override fun onSetVariable(binding: VB, viewModel: VM?): Boolean {
                return this@RecyclerViewBindingAdapter.onSetVariable(binding, viewModel)
            }

        }
    }

    override fun onBindViewHolder(holder: BindingHolder<VB>, position: Int) {

        bindingHelper.bind(holder, position, getItem(position))
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<VB> {
        return BindingHolder<VB>(bindingHelper.createBinding(parent, viewType))
    }

    override fun onViewAttachedToWindow(holder: BindingHolder<VB>) {
        bindingHelper.onViewAttachedToWindow(holder)
    }

    /**
     * 在这里设置 ViewDataBinding::setVariable(int variableId, @Nullable Object value);
     */
    open fun onSetVariable(binding: ViewDataBinding, viewModel: VM?): Boolean {
        val appTheme = ViewTreeAppThemeViewModel[binding.root]
        binding.setVariable(com.pp.theme.BR.dynamicThemeViewModel, appTheme)

        val lifecycleOwner = ViewTreeLifecycleOwner.get(binding.root)
        binding.lifecycleOwner = lifecycleOwner
        return false
    }

    /**
     * 创建viewModel
     */
    abstract fun createViewModel(
        binding: VB,
        item: T?,
        cacheItemViewModel: VM?,
    ): VM?

    /**
     * 创建viewType类型的ViewDataBinding
     */
    abstract fun onCreateBinding(parent: ViewGroup, viewType: Int): VB


}

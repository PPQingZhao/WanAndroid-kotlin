package com.pp.ui.adapter

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.pp.ui.BR

abstract class BindingItemViewModel<VB : ViewDataBinding, VM : Any?, Data : Any> {

    abstract fun modelClazz(): Class<VM>
    fun bindItemViewModel(
        binding: VB,
        data: Data,
        position: Int,
        cachedItemViewModel: VM?,
    ): VM {
        val a: Class<String> = String::class.java
        return onBindItemViewModel(binding, data, position, cachedItemViewModel).apply {
            setVariable(binding, this)
        }
    }

    abstract fun onBindItemViewModel(
        binding: VB,
        data: Data?,
        position: Int,
        cachedItemViewModel: VM?,
    ): VM

    fun setVariable(binding: VB, viewModel: VM) {
        try {
            val result = onSetVariable(binding, viewModel)
            if (!result) {
                //set default variable
                binding.setVariable(BR.viewModel, viewModel)
            }
        } catch (e: ClassCastException) {
            e.printStackTrace()
        } finally {
            binding.executePendingBindings()
        }
    }

    open fun onSetVariable(binding: VB, viewModel: VM): Boolean {
        return false
    }

    @LayoutRes
    abstract fun getLayoutRes(): Int;

}
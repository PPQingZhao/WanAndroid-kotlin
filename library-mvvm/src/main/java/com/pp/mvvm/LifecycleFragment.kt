package com.pp.mvvm

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

abstract class LifecycleFragment<VB : ViewDataBinding, VM : LifecycleViewModel> : Fragment() {

    abstract val mBinding: VB

    val mViewModel: VM by lazy { ViewModelProvider(this, getModelFactory())[getModelClazz()] }

    open fun getModelFactory(): ViewModelProvider.Factory =
        ViewModelProvider.AndroidViewModelFactory(activity?.application!!)

    abstract fun getModelClazz(): Class<VM>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(mViewModel)
        setVariable(mBinding, mViewModel)
        mBinding.setLifecycleOwner {
            viewLifecycleOwner.lifecycle
        }

    }

    private fun setVariable(binding: VB, viewModel: VM) {
        if (!onSetVariable(binding, viewModel)) {
            // set default variable
            try {
                binding.setVariable(BR.viewModel, viewModel)
            } catch (e: ClassCastException) {
                e.printStackTrace()
            }
        }
    }

    open fun onSetVariable(binding: VB, viewModel: VM): Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val parent = mBinding.root.parent
        if (parent is ViewGroup) {
            parent.removeView(mBinding.root)
        }
        return mBinding.root
    }

    private var alreadyResume = false
    override fun onResume() {
        super.onResume()
        if (!alreadyResume) {
            Log.e("TAG", "onFirstResume==> ${this}")
            onFirstResume()
            alreadyResume = true
        }
    }

    open fun onFirstResume() {
    }

}
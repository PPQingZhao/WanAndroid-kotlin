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

    private val DEBUG = BuildConfig.DEBUG
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
        savedInstanceState: Bundle?,
    ): View? {
        val parent = mBinding.root.parent
        if (parent is ViewGroup) {
            parent.removeView(mBinding.root)
        }
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        if (DEBUG) {
            Log.e("TAG", "onStart==> $this")
        }
    }

    override fun onPause() {
        super.onPause()
        if (DEBUG) {
            Log.e("TAG", "onPause==> $this")
        }
    }

    private var alreadyResume = false
    override fun onResume() {
        super.onResume()
        if (DEBUG) {
            Log.e("TAG", "onResume==> $this")
        }
        if (!alreadyResume) {
            if (DEBUG) {
                Log.e("TAG", "onFirstResume==> $this")
            }
            onFirstResume()
            alreadyResume = true
        }
    }

    override fun onStop() {
        super.onStop()
        if (DEBUG) {
            Log.e("TAG", "onStop==> $this")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (DEBUG) {
            Log.e("TAG", "onDestroyView ==> ${this}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (DEBUG) {
            Log.e("TAG", "destroy ===> ${this}")
        }
    }

    open fun onFirstResume() {
    }

}
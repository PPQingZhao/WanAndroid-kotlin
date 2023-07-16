package com.pp.mvvm

import android.os.Bundle
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider

abstract class LifecycleActivity<VB : ViewDataBinding, VM : LifecycleViewModel> :
    AppCompatActivity() {
    abstract val mBinding: VB

    val mViewModel by lazy { ViewModelProvider(this, getModelFactory())[(getModelClazz())] }

    abstract fun getModelClazz(): Class<VM>

    open fun getModelFactory(): ViewModelProvider.Factory =
        ViewModelProvider.AndroidViewModelFactory(application!!)

    private var windowInsets = MutableLiveData<WindowInsets?>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        /*
            windowInsets分发:拦截WindowInsets, 将windowInsets分发给每个fragment
            沉浸式状态栏: window flags = LayoutParams.FLAG_TRANSLUCENT_STATUS 配合布局中 android:fitsSystemWindows="true"进行实现
        */

        ActivityWindowInsetsDispatcher.dispatch(this)

        lifecycle.addObserver(mViewModel)
        mBinding.setLifecycleOwner { this.lifecycle }
//        ViewTreeLifecycleOwner.set(mBinding.root, this)
//        ViewTreeViewModelStoreOwner.set(mBinding.root, this)

        setVariable(mBinding, mViewModel)
    }

    private fun setVariable(binding: VB, viewModel: VM) {
        try {
            if (!onSetVariable(binding, viewModel)) {
                // set default variable
                binding.setVariable(BR.viewModel, viewModel)
            }
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
    }

    open fun onSetVariable(binding: VB, viewModel: VM): Boolean = false


}
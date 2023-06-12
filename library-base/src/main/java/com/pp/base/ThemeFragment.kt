package com.pp.library_base.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import com.pp.theme.BR
import com.pp.mvvm.LifecycleFragment
import com.pp.theme.DynamicTheme
import com.pp.theme.ViewTreeAppThemeViewModel

/**
 * theme fragment
 */
abstract class ThemeFragment<VB : ViewDataBinding, VM : ThemeViewModel> :
    LifecycleFragment<VB, VM>() {

    fun requireTheme(): DynamicTheme {
        return (requireActivity() as ThemeActivity<*, *>).mThemeViewModel
    }

    @CallSuper
    override fun onSetVariable(binding: VB, viewModel: VM): Boolean {
        // set theme variable
        mBinding.setVariable(BR.dynamicThemeViewModel, requireTheme())
        return super.onSetVariable(binding, viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        ViewTreeAppThemeViewModel[mBinding.root] = requireTheme()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

}
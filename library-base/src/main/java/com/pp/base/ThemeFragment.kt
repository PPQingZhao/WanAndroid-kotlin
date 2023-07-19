package com.pp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import com.pp.theme.BR
import com.pp.mvvm.LifecycleFragment
import com.pp.theme.AppDynamicTheme
import com.pp.theme.DynamicTheme
import com.pp.theme.ViewTreeAppThemeViewModel

/**
 * theme fragment
 */
abstract class ThemeFragment<VB : ViewDataBinding, VM : ThemeViewModel> :
    LifecycleFragment<VB, VM>() {

    /**
     * fragment主题默认与宿主activity主题相同
     * 可重写该函数,自定义主题
     */
    open fun requireTheme(): DynamicTheme? {
        return (requireActivity() as ThemeActivity<*, *>).getDynamicTheme()
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
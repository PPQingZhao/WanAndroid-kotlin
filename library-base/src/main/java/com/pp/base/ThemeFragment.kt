package com.pp.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.pp.mvvm.LifecycleFragment
import com.pp.theme.*
import com.pp.theme.BR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * theme fragment
 */
abstract class ThemeFragment<VB : ViewDataBinding, VM : ThemeViewModel> :
    LifecycleFragment<VB, VM>() {

    open var dynamicTheme: AppDynamicTheme? = AppDynamicTheme()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        lifecycleScope.launch(Dispatchers.IO) {
            dynamicTheme?.collectTheme(
                themeFactory(
                    requireActivity().theme,
                    resources.displayMetrics,
                    resources.configuration
                )
            )
        }
    }

    @CallSuper
    override fun onSetVariable(binding: VB, viewModel: VM): Boolean {
        // set theme variable
        mBinding.setVariable(BR.dynamicThemeViewModel, dynamicTheme)
        return super.onSetVariable(binding, viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        ViewTreeAppThemeViewModel[mBinding.root] = dynamicTheme
        return super.onCreateView(inflater, container, savedInstanceState)
    }

}
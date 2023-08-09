package com.pp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.pp.mvvm.LifecycleFragment
import com.pp.theme.ViewTreeAppThemeViewModel
import com.pp.theme.collectTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * theme fragment
 */
abstract class ThemeFragment<VB : ViewDataBinding, VM : ThemeViewModel> :
    LifecycleFragment<VB, VM>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO) {
            mViewModel.mTheme.collectTheme(
                themeFactory(
                    requireActivity().theme,
                    resources.displayMetrics,
                    resources.configuration
                )
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState).apply {
            ViewTreeAppThemeViewModel[mBinding.root] = mViewModel.mTheme
        }
    }

}
package com.pp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.ViewDataBinding
import com.pp.mvvm.LifecycleFragment
import com.pp.theme.ViewTreeAppThemeViewModel

/**
 * theme fragment
 */
abstract class ThemeFragment<VB : ViewDataBinding, VM : ThemeViewModel> :
    LifecycleFragment<VB, VM>() {

    private var onBackPressCallback: OnBackPressedCallback? = null

    protected fun enableBackPressed(enable: Boolean) {
        if (!enable && null == onBackPressCallback) {
            return
        }
        getOnBackPressedCallback().isEnabled = enable
    }

    private fun getOnBackPressedCallback(): OnBackPressedCallback {
        if (null == onBackPressCallback) {
            onBackPressCallback = object : OnBackPressedCallback(false) {
                override fun handleOnBackPressed() {
                    this@ThemeFragment.handleOnBackPressed()
                }
            }
        }
        return onBackPressCallback!!
    }

    protected open fun handleOnBackPressed() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher
            .addCallback(this, getOnBackPressedCallback())
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
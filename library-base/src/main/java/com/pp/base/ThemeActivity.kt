package com.pp.base

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.pp.mvvm.LifecycleActivity
import com.pp.theme.ViewTreeAppThemeViewModel
import com.pp.theme.collectTheme
import com.pp.theme.init
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * theme activity
 */
abstract class ThemeActivity<VB : ViewDataBinding, VM : ThemeViewModel> :
    LifecycleActivity<VB, VM>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.mTheme.init(this)
        ViewTreeAppThemeViewModel[mBinding.root] = mViewModel.mTheme
        lifecycleScope.launch(Dispatchers.IO) {
            mViewModel.mTheme.collectTheme(
                themeFactory(theme, resources.displayMetrics, resources.configuration)
            )
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setTranslucent()
    }

    /**
     * 设置状态栏字体颜色
     */
    fun requireLightStatusBar(light: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                if (light) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =
                if (light) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else View.VISIBLE
        }

    }

    private fun setTranslucent() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }
}
package com.pp.base

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.core.graphics.ColorUtils
import androidx.databinding.ViewDataBinding
import com.pp.mvvm.LifecycleActivity
import com.pp.theme.*

/**
 * theme activity
 */
abstract class ThemeActivity<VB : ViewDataBinding, VM : ThemeViewModel> :
    LifecycleActivity<VB, VM>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.mTheme.init(this)

        applySkinTheme(mViewModel)

        ViewTreeAppThemeViewModel[mBinding.root] = mViewModel.mTheme

        /**
         * 应用主要色调(toolbar)
         */
        mViewModel.mTheme.colorPrimary.observe(this) { it ->
            // 计算颜色亮度
            val luminance = ColorUtils.calculateLuminance(it.defaultColor)
//            Log.e("TAG", "luminance: $luminance")
            // 亮度大于0.5,则认为主题色为亮色,需设置状态栏亮色(字体深色)
            requireLightStatusBar(luminance > 0.5)

        }
    }

    fun applySkinTheme(viewModel: ThemeViewModel) {
        viewModel.applySkinTheme(theme)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setTranslucent()
    }

    /**
     * 设置状态栏深色(字体高亮)或亮色(字体深色)
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
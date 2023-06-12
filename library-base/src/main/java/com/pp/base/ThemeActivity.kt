package com.pp.library_base.base

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.databinding.ViewDataBinding
import com.pp.mvvm.LifecycleActivity
import com.pp.theme.DynamicTheme
import com.pp.theme.DynamicThemeDataBinding

/**
 * theme activity
 */
abstract class ThemeActivity<VB : ViewDataBinding, VM : ThemeViewModel> :
    LifecycleActivity<VB, VM>() {

    val mThemeViewModel by lazy { getDynamicThemeDataBinding().mThemeViewModel }
    private var dynamicThemeDataBinding: DynamicThemeDataBinding? = null
    private fun getDynamicThemeDataBinding(): DynamicThemeDataBinding {
        if (null == dynamicThemeDataBinding) {
            dynamicThemeDataBinding = DynamicThemeDataBinding(mBinding, window)
        }
        return dynamicThemeDataBinding!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDynamicThemeDataBinding().binding(this)
    }

    override fun onApplyThemeResource(theme: Resources.Theme?, resid: Int, first: Boolean) {
        super.onApplyThemeResource(theme, resid, first)

//        Log.e("TAG", "onApplyThemeResource")
        theme?.apply {
            getDynamicThemeDataBinding().mThemeViewModel.setTheme(this)
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
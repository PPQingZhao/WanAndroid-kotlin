package com.pp.local.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.pp.base.ThemeFragment
import com.pp.local.databinding.FragmentThemeSettingBinding
import com.pp.local.viewmodel.ThemeSettingViewModel
import com.pp.router_service.RouterPath

@Route(path = RouterPath.Local.fragment_theme_setting)
class ThemeSettingFragment : ThemeFragment<FragmentThemeSettingBinding, ThemeSettingViewModel>() {
    override val mBinding: FragmentThemeSettingBinding by lazy {
        FragmentThemeSettingBinding.inflate(
            layoutInflater
        )
    }

    override fun getModelClazz(): Class<ThemeSettingViewModel> {
        return ThemeSettingViewModel::class.java
    }
}
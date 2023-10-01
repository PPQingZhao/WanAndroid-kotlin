package com.pp.local.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.pp.base.ThemeFragment
import com.pp.local.databinding.FragmentSettingBinding
import com.pp.router_service.RouterPath

@Route(path = RouterPath.Local.fragment_setting)
class SettingFragment : ThemeFragment<FragmentSettingBinding, SettingViewModel>() {
    override val mBinding: FragmentSettingBinding by lazy {
        FragmentSettingBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<SettingViewModel> {
        return SettingViewModel::class.java
    }
}
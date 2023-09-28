package com.pp.local.ui

import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.transition.MaterialSharedAxis
import com.pp.base.ThemeFragment
import com.pp.common.util.materialSharedAxis
import com.pp.local.databinding.FragmentThemeSettingBinding
import com.pp.local.model.ItemPreferenceThemeSettingViewModel
import com.pp.router_service.RouterPath
import com.pp.theme.AppDynamicTheme
import com.pp.theme.DynamicThemeManager
import com.pp.theme.getDynamicTheme

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = materialSharedAxis(MaterialSharedAxis.X, true)
        exitTransition = materialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onFirstResume() {
        val dataList = mutableListOf<ItemPreferenceThemeSettingViewModel>()
        DynamicThemeManager.getSkinTheme().onEach {
            ItemPreferenceThemeSettingViewModel(
                it,
                getDynamicTheme<AppDynamicTheme>("Theme.Dynamic",
                    DisplayMetrics().apply {
                        setTo(requireActivity().theme.resources.displayMetrics)
                    },
                    Configuration().apply {
                        setTo(requireActivity().theme.resources.configuration)
                    }),
            ).apply {
                viewLifecycleOwner.lifecycle.addObserver(this)
                dataList.add(this)
            }
        }
        mViewModel.mAdapter.setDataList(dataList)
    }

}
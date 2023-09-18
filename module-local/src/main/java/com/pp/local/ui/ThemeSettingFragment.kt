package com.pp.local.ui

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.transition.MaterialSharedAxis
import com.pp.base.ThemeFragment
import com.pp.base.WanAndroidTheme
import com.pp.common.util.materialSharedAxis
import com.pp.local.databinding.FragmentThemeSettingBinding
import com.pp.local.databinding.ItemThemeSettingBinding
import com.pp.local.model.ItemPreferenceThemeSettingViewModel
import com.pp.router_service.RouterPath
import com.pp.ui.adapter.ItemViewModelBinder
import com.pp.ui.adapter.RecyclerViewBindingAdapter
import com.pp.ui.adapter.createItemViewModelBinder

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
        initRecyclerView()
    }

    private val mAdapter by lazy {

        RecyclerViewBindingAdapter<ItemPreferenceThemeSettingViewModel>(getItemLayoutRes = { com.pp.local.R.layout.item_theme_setting })
            .apply {

                createItemViewModelBinder<ItemThemeSettingBinding, ItemPreferenceThemeSettingViewModel, ItemPreferenceThemeSettingViewModel>(
                    getItemViewModel = {
                        it.invoke()
                    }).let {
                        addItemViewModelBinder(it)
                }

            }
    }

    private fun initRecyclerView() {
        mBinding.recyclerview.layoutManager = GridLayoutManager(context, 3)
        mBinding.recyclerview.adapter = mAdapter
        mAdapter.setDataList(getThemes())
    }

    private fun getThemes(): List<ItemPreferenceThemeSettingViewModel> {
        val dataList = mutableListOf<ItemPreferenceThemeSettingViewModel>()
        dataList.add(
            ItemPreferenceThemeSettingViewModel(
                WanAndroidTheme.Default,
                requireActivity().theme,
                resources.displayMetrics,
                resources.configuration
            )
        )
        dataList.add(
            ItemPreferenceThemeSettingViewModel(
                WanAndroidTheme.Black,
                requireActivity().theme,
                resources.displayMetrics,
                resources.configuration
            )
        )
        dataList.add(
            ItemPreferenceThemeSettingViewModel(
                WanAndroidTheme.Blue,
                requireActivity().theme,
                resources.displayMetrics,
                resources.configuration
            )
        )

        dataList.onEach {
            lifecycle.addObserver(it)
        }
        return dataList
    }


}
package com.pp.local.ui

import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.pp.base.ThemeFragment
import com.pp.base.WanAndroidTheme
import com.pp.local.databinding.FragmentThemeSettingBinding
import com.pp.local.databinding.ItemThemeSettingBinding
import com.pp.local.model.ItemPreferenceThemeSettingViewModel
import com.pp.router_service.RouterPath
import com.pp.ui.adapter.RecyclerViewBindingAdapter

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
        initRecyclerView()
    }

    private val mAdapter by lazy {
        object :
            RecyclerViewBindingAdapter<ItemThemeSettingBinding, ItemPreferenceThemeSettingViewModel, ItemPreferenceThemeSettingViewModel>() {
            override fun createViewModel(
                binding: ItemThemeSettingBinding,
                item: ItemPreferenceThemeSettingViewModel?,
                cacheItemViewModel: ItemPreferenceThemeSettingViewModel?,
            ): ItemPreferenceThemeSettingViewModel? {
                return item
            }

            override fun onCreateBinding(
                parent: ViewGroup,
                viewType: Int,
            ): ItemThemeSettingBinding {
                return ItemThemeSettingBinding.inflate(layoutInflater, parent, false)
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
                resources.newTheme(),
                resources.displayMetrics,
                resources.configuration
            )
        )
        dataList.add(
            ItemPreferenceThemeSettingViewModel(
                WanAndroidTheme.Black,
                resources.newTheme(),
                resources.displayMetrics,
                resources.configuration
            )
        )
        dataList.add(
            ItemPreferenceThemeSettingViewModel(
                WanAndroidTheme.Blue,
                resources.newTheme(),
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
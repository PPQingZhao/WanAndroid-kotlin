package com.pp.local.ui

import android.app.Application
import com.pp.base.ThemeViewModel
import com.pp.local.R
import com.pp.local.databinding.ItemThemeSettingBinding
import com.pp.local.model.ItemPreferenceThemeSettingViewModel
import com.pp.ui.adapter.RecyclerViewBindingAdapter
import com.pp.ui.adapter.createItemViewModelBinder

class ThemeSettingViewModel(app: Application) : ThemeViewModel(app) {

    val mAdapter by lazy {

        RecyclerViewBindingAdapter<ItemPreferenceThemeSettingViewModel>(getItemLayoutRes = { R.layout.item_theme_setting })
            .apply {

                createItemViewModelBinder<ItemThemeSettingBinding, ItemPreferenceThemeSettingViewModel, ItemPreferenceThemeSettingViewModel>(
                    getItemViewModel = { it })
                    .let {
                        addItemViewModelBinder(it)
                    }

            }
    }

}
package com.pp.local.ui

import android.app.Application
import android.view.View
import com.pp.base.ThemeViewModel
import com.pp.common.router.MultiRouterFragmentViewModel
import com.pp.local.R
import com.pp.local.databinding.ItemThemeSettingBinding
import com.pp.local.model.ItemPreferenceThemeSettingViewModel
import com.pp.router_service.RouterPath
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

    fun onBack(view: View) {
        MultiRouterFragmentViewModel.popBackStack(view, RouterPath.Local.fragment_theme_setting)
    }

}
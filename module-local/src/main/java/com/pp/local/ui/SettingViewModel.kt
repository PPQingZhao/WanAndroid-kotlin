package com.pp.local.ui

import android.app.Application
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.pp.base.ThemeViewModel
import com.pp.common.model.ItemCheckedLocalViewModel
import com.pp.common.model.ItemTextAllowRightLocalViewModel
import com.pp.common.repository.UserRepository
import com.pp.common.router.MultiRouterFragmentViewModel
import com.pp.ui.R
import com.pp.router_service.RouterPath
import com.pp.ui.adapter.RecyclerViewBindingAdapter
import com.pp.ui.adapter.createItemViewModelBinder
import com.pp.ui.databinding.ItemCheckedBinding
import com.pp.ui.databinding.ItemTextAllowRightBinding
import com.pp.ui.viewModel.ItemDataViewModel
import com.pp.ui.viewModel.ItemLocalBean
import com.pp.ui.viewModel.OnItemListener
import kotlinx.coroutines.launch

class SettingViewModel(app: Application) : ThemeViewModel(app) {

    fun onBack(view: View) {
        MultiRouterFragmentViewModel.popBackStack(view, RouterPath.Local.fragment_setting)
    }

    val mAdapter =
        RecyclerViewBindingAdapter<ItemLocalBean>(getItemLayoutRes = {
            it!!.itemType
        }).apply {
            val onItemListener = object : OnItemListener<ItemDataViewModel<ItemLocalBean>> {
                override fun onItemClick(
                    view: View,
                    item: ItemDataViewModel<ItemLocalBean>,
                ): Boolean {
                    when (item.data?.itemType) {
                        R.layout.item_text_allow_right -> {
                            viewModelScope.launch {
                                UserRepository.logoutWithPreferenceClear()
                                remove(item.data)
                            }
                        }
                    }
                    return true
                }
            }
            val itemTextAllowRightLocalBinder =
                createItemViewModelBinder<ItemTextAllowRightBinding, ItemLocalBean, ItemTextAllowRightLocalViewModel>(
                    getItemViewModel = {
                        ItemTextAllowRightLocalViewModel(it, mTheme).apply {
                            setOnItemListener(onItemListener)
                        }
                    })

            val itemCheckedLocalBinder =
                createItemViewModelBinder<ItemCheckedBinding, ItemLocalBean, ItemCheckedLocalViewModel>(
                    getItemViewModel = {
                        ItemCheckedLocalViewModel(viewModelScope,it, mTheme).apply {
                        }
                    })

            addItemViewModelBinder(itemTextAllowRightLocalBinder)
            addItemViewModelBinder(itemCheckedLocalBinder)

        }

    override fun onFirstResume(owner: LifecycleOwner) {
        viewModelScope.launch {
            val dataList = mutableListOf<ItemLocalBean>().apply {
                add(
                    ItemLocalBean(
                        itemType = R.layout.item_checked,
                        leftText = R.string.setting_floating_theme
                    )
                )
                if (UserRepository.getPreferenceUser() != null) {
                    add(
                        ItemLocalBean(
                            itemType = R.layout.item_text_allow_right,
                            leftText = R.string.logout
                        )
                    )
                }
            }

            mAdapter.setDataList(dataList)
        }
    }


}
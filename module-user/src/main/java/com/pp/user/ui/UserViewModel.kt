package com.pp.user.ui

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.transition.MaterialSharedAxis
import com.pp.base.ThemeViewModel
import com.pp.common.router.MultiRouterFragmentViewModel
import com.pp.common.util.ViewTreeMultiRouterFragmentViewModel
import com.pp.common.util.materialSharedAxis
import com.pp.database.user.User
import com.pp.router_service.RouterPath
import com.pp.ui.R
import com.pp.ui.adapter.RecyclerViewBindingAdapter
import com.pp.ui.adapter.createItemViewModelBinder
import com.pp.ui.databinding.ItemAllowRightBinding
import com.pp.ui.viewModel.ItemAllowRightViewModel
import com.pp.ui.viewModel.ItemDataViewModel
import com.pp.ui.viewModel.OnItemListener
import com.pp.user.manager.UserManager

class UserViewModel(app: Application) : ThemeViewModel(app) {
    val userModel = UserManager.userModel()

    val mAdapter by lazy {
        RecyclerViewBindingAdapter<ItemAllowRightViewModel>(getItemLayoutRes = { R.layout.item_allow_right })
            .apply {

                createItemViewModelBinder<ItemAllowRightBinding, ItemAllowRightViewModel, ItemAllowRightViewModel>(
                    getItemViewModel = { it }
                ).also {
                    addItemViewModelBinder(it)
                }
            }

    }

    private fun getItems(): List<ItemAllowRightViewModel> {
        return mutableListOf<ItemAllowRightViewModel>().apply {
            add(
                ItemAllowRightViewModel(
                    com.pp.skin.R.drawable.ic_message_center,
                    R.string.message_center,
                    mTheme
                )
            )
            add(
                ItemAllowRightViewModel(
                    com.pp.skin.R.drawable.ic_share,
                    R.string.share_articles,
                    mTheme
                )
            )

            add(
                ItemAllowRightViewModel(
                    com.pp.skin.R.drawable.ic_favorite_on,
                    R.string.collect_articles,
                    mTheme
                ).apply {
                    setOnItemListener(object : OnItemListener<ItemDataViewModel<Any>> {
                        override fun onItemClick(
                            view: View,
                            item: ItemDataViewModel<Any>,
                        ): Boolean {
                            ViewTreeMultiRouterFragmentViewModel.get<MultiRouterFragmentViewModel>(
                                view
                            )?.showFragment(
                                RouterPath.Local.fragment_collected,
                                RouterPath.Local.fragment_collected,
                                mainExitTransition = materialSharedAxis(MaterialSharedAxis.X, true),
                                mainReenterTransition = materialSharedAxis(
                                    MaterialSharedAxis.X,
                                    false
                                )
                            )
                            return true
                        }
                    })
                }
            )

            add(
                ItemAllowRightViewModel(
                    com.pp.skin.R.drawable.ic_skin,
                    R.string.theme_setting,
                    mTheme
                ).apply {
                    setOnItemListener(object : OnItemListener<ItemDataViewModel<Any>> {
                        override fun onItemClick(
                            view: View,
                            item: ItemDataViewModel<Any>,
                        ): Boolean {
                            ViewTreeMultiRouterFragmentViewModel.get<MultiRouterFragmentViewModel>(
                                view
                            )?.showFragment(
                                RouterPath.Local.fragment_theme_setting,
                                RouterPath.Local.fragment_theme_setting,
                                mainExitTransition = materialSharedAxis(MaterialSharedAxis.X, true),
                                mainReenterTransition = materialSharedAxis(
                                    MaterialSharedAxis.X,
                                    false
                                )
                            )
                            return true
                        }
                    })
                }
            )

        }
    }

    override fun onFirstResume(owner: LifecycleOwner) {
        mAdapter.setDataList(getItems())
    }

    fun nickName(user: User?): String {
        return "用户名: ${user?.nickName}"
    }

    fun userId(user: User?): String {
        return "id: ${user?.userId}"
    }

    fun coinInfo(user: User?): String {
        return "积分:${user?.coinCount} 等级:${user?.level} 排名:${user?.rank}"
    }
}
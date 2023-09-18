package com.pp.user.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.transition.MaterialSharedAxis
import com.pp.base.ThemeFragment
import com.pp.common.router.MultiRouterFragmentViewModel
import com.pp.common.util.ViewTreeMultiRouterFragmentViewModel
import com.pp.common.util.materialSharedAxis
import com.pp.router_service.RouterPath
import com.pp.ui.adapter.ItemViewModelBinder
import com.pp.ui.adapter.RecyclerViewBindingAdapter
import com.pp.ui.adapter.createItemDataBinder
import com.pp.ui.adapter.createItemViewModelBinder
import com.pp.ui.databinding.ItemAllowRightBinding
import com.pp.ui.viewModel.ItemAllowRightViewModel
import com.pp.ui.viewModel.ItemDataViewModel
import com.pp.ui.viewModel.OnItemListener
import com.pp.user.R
import com.pp.user.databinding.FragmentUserBinding
import com.pp.user.model.UserItemAllowRightModel

@Route(path = RouterPath.User.fragment_user)
class UserFragment : ThemeFragment<FragmentUserBinding, UserViewModel>() {
    override val mBinding: FragmentUserBinding by lazy {
        FragmentUserBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<UserViewModel> {
        return UserViewModel::class.java
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private val mAdapter by lazy {

        RecyclerViewBindingAdapter<ItemAllowRightViewModel>(getItemLayoutRes = { com.pp.ui.R.layout.item_allow_right })
            .apply {

                createItemViewModelBinder<ItemAllowRightBinding, ItemAllowRightViewModel, ItemAllowRightViewModel>(
                    getItemViewModel = {
                        it.invoke()
                    }
                ).also {
                    addItemViewModelBinder(it)
                }
            }

    }

    private fun initRecyclerView() {
        mBinding.userRecyclerview.layoutManager = LinearLayoutManager(context)
        mAdapter.setDataList(getItems())
        mBinding.userRecyclerview.adapter = mAdapter
    }

    private fun getItems(): List<ItemAllowRightViewModel> {
        return mutableListOf<ItemAllowRightViewModel>().apply {
            add(
                ItemAllowRightViewModel(
                    com.pp.skin.R.drawable.ic_message_center,
                    R.string.message_center,
                    mViewModel.mTheme
                )
            )
            add(
                ItemAllowRightViewModel(
                    com.pp.skin.R.drawable.ic_share,
                    R.string.share_articles,
                    mViewModel.mTheme
                )
            )

            add(
                ItemAllowRightViewModel(
                    com.pp.skin.R.drawable.ic_favorite_on,
                    R.string.collect_articles,
                    mViewModel.mTheme
                ).apply {
                    setOnItemListener(object : OnItemListener<ItemDataViewModel<Any>> {
                        override fun onItemClick(
                            view: View,
                            item: ItemDataViewModel<Any>,
                        ): Boolean {
                            ViewTreeMultiRouterFragmentViewModel.get<MultiRouterFragmentViewModel>(
                                mBinding.root
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
                    mViewModel.mTheme
                ).apply {
                    setOnItemListener(object : OnItemListener<ItemDataViewModel<Any>> {
                        override fun onItemClick(
                            view: View,
                            item: ItemDataViewModel<Any>,
                        ): Boolean {
                            ViewTreeMultiRouterFragmentViewModel.get<MultiRouterFragmentViewModel>(
                                mBinding.root
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

}
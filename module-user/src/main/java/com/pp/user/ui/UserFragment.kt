package com.pp.user.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.pp.base.ThemeFragment
import com.pp.router_service.RouterPath
import com.pp.ui.adapter.RecyclerViewBindingAdapter
import com.pp.ui.databinding.ItemAllowRightBinding
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
        object :
            RecyclerViewBindingAdapter<ItemAllowRightBinding, UserItemAllowRightModel, UserItemAllowRightModel>() {
            override fun createViewModel(
                binding: ItemAllowRightBinding,
                item: UserItemAllowRightModel?,
                cacheItemViewModel: UserItemAllowRightModel?,
            ): UserItemAllowRightModel? {
                return item
            }

            override fun onCreateBinding(
                parent: ViewGroup,
                viewType: Int,
            ): ItemAllowRightBinding {
                return ItemAllowRightBinding.inflate(layoutInflater, parent, false)
            }
        }
    }

    private fun initRecyclerView() {
        mBinding.userRecyclerview.layoutManager = LinearLayoutManager(context)
        mAdapter.setDataList(getItems())
        mBinding.userRecyclerview.adapter = mAdapter
    }

    private fun getItems(): List<UserItemAllowRightModel> {
        return mutableListOf<UserItemAllowRightModel>().apply {
            add(
                UserItemAllowRightModel(
                    com.pp.skin.R.drawable.ic_message_center,
                    R.string.message_center,
                    mViewModel.mTheme
                )
            )
            add(
                UserItemAllowRightModel(
                    com.pp.skin.R.drawable.ic_share,
                    R.string.share_articles,
                    mViewModel.mTheme
                )
            )

            add(
                UserItemAllowRightModel(
                    com.pp.skin.R.drawable.ic_favorite,
                    R.string.collect_articles,
                    mViewModel.mTheme
                )
            )
            add(
                UserItemAllowRightModel(
                    com.pp.skin.R.drawable.ic_tool,
                    R.string.tool_list,
                    mViewModel.mTheme
                )
            )

        }
    }

}
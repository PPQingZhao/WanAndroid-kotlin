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
import com.pp.ui.R
import com.pp.ui.adapter.RecyclerViewBindingAdapter
import com.pp.ui.adapter.createItemViewModelBinder
import com.pp.ui.databinding.ItemAllowRightBinding
import com.pp.ui.viewModel.ItemAllowRightViewModel
import com.pp.ui.viewModel.ItemDataViewModel
import com.pp.ui.viewModel.OnItemListener
import com.pp.user.databinding.FragmentUserBinding

@Route(path = RouterPath.User.fragment_user)
class UserFragment : ThemeFragment<FragmentUserBinding, UserViewModel>() {
    override val mBinding: FragmentUserBinding by lazy {
        FragmentUserBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<UserViewModel> {
        return UserViewModel::class.java
    }

}
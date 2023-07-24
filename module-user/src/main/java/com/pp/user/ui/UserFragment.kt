package com.pp.user.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.pp.base.ThemeFragment
import com.pp.router_service.RouterPath
import com.pp.user.databinding.FragmentUserBinding

@Route(path = RouterPath.User.fragment_user)
class UserFragment : ThemeFragment<FragmentUserBinding, UserViewModel>() {
    override val mBinding: FragmentUserBinding by lazy {
        FragmentUserBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<UserViewModel> {
        return UserViewModel::class.java
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.btnTheme.setOnClickListener {
        }

        mBinding.btnLogin.setOnClickListener {
            LoginAndRegisterActivity.start(requireActivity())
        }
    }

}
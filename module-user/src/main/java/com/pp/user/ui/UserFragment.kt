package com.pp.user.ui

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.pp.base.ThemeFragment
import com.pp.base.WanAndroidTheme
import com.pp.base.getPreferenceTheme
import com.pp.base.updateTheme
import com.pp.router_service.RouterPath
import com.pp.user.databinding.FragmentUserBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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

        var themeId = WanAndroidTheme.Default
        lifecycleScope.launch(Dispatchers.IO) {
            getPreferenceTheme().collectLatest {
                themeId = it ?: WanAndroidTheme.Default
            }
        }

        mBinding.btnTheme.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                if (themeId == WanAndroidTheme.Default) {
                    updateTheme(WanAndroidTheme.Black)
                } else if (themeId == WanAndroidTheme.Black) {
                    updateTheme(WanAndroidTheme.Blue)
                } else {
                    updateTheme(WanAndroidTheme.Default)
                }
            }
        }

        mBinding.btnLogin.setOnClickListener {
            LoginAndRegisterActivity.start(requireActivity())
        }
    }

}
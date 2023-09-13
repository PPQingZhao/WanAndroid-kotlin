package com.pp.user.ui

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.transition.MaterialSharedAxis
import com.pp.base.ThemeFragment
import com.pp.common.app.App
import com.pp.common.router.MultiRouterFragmentViewModel
import com.pp.common.util.ViewTreeMultiRouterFragmentViewModel
import com.pp.common.util.materialSharedAxis
import com.pp.router_service.RouterPath
import com.pp.user.databinding.FragmentLoginAndRegisterBinding
import kotlinx.coroutines.launch

@Route(path = RouterPath.User.fragment_login)
class LoginAndRegisterFragment :
    ThemeFragment<FragmentLoginAndRegisterBinding, LoginAndRegisterViewModel>() {


    override val mBinding: FragmentLoginAndRegisterBinding by lazy {
        FragmentLoginAndRegisterBinding.inflate(
            layoutInflater
        )
    }

    override fun getModelClazz(): Class<LoginAndRegisterViewModel> {
        return LoginAndRegisterViewModel::class.java
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = materialSharedAxis(MaterialSharedAxis.X, true)
        exitTransition = materialSharedAxis(MaterialSharedAxis.X, false)

        initLoginViewModel()
        initRegisterViewModel()
        initMotionLayout()
        initView()
    }

    private fun initView() {
        mBinding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun onBackPressed() {
        ViewTreeMultiRouterFragmentViewModel.get<MultiRouterFragmentViewModel>(
            mBinding.root
        )?.run {
            popBackStack(RouterPath.User.fragment_login)
        }
    }

    private fun initMotionLayout() {
    }

    private fun initRegisterViewModel() {
        // 注册成功,更新并且显示 登录ui
        lifecycleScope.launch {
            mViewModel.registerViewModel.registerResult.collect {
                if (it) {
                    // 更新登录ui
                    mViewModel.loginViewModel.username.value =
                        mViewModel.registerViewModel.username.value
                    mViewModel.loginViewModel.password.value =
                        mViewModel.registerViewModel.password.value
                    // 显示登录ui
                    showLoginView()
                }
            }
        }

        lifecycleScope.launch {
            // 注册页面点击返回按钮,隐藏注册ui,显示登录ui
            mViewModel.registerViewModel.onReturn.collect {
                if (it) {
                    showLoginView()
                }
            }
        }
    }

    private fun initLoginViewModel() {
        lifecycleScope.launch {
            mViewModel.loginViewModel.loginResult.collect {
                if (it) {
                    ViewTreeMultiRouterFragmentViewModel.get<MultiRouterFragmentViewModel>(
                        mBinding.root
                    )?.run {
                        popBackStack(RouterPath.User.fragment_login)
                    }
                }
            }
        }

        lifecycleScope.launch {
            mViewModel.loginViewModel.onNewUser.collect {
                if (it) {
                    mViewModel.registerViewModel.reset()
                    showRegisterView()
                }
            }
        }
    }

    private fun showLoginView() {
        mBinding.motionLayout.addTransitionListener(object : TransitionAdapter() {
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                mBinding.motionLayout.removeTransitionListener(this)
                mBinding.viewRegister.root.visibility = View.GONE
            }

            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
            ) {
                mBinding.viewLogin.root.visibility = View.VISIBLE
            }
        })
        mBinding.motionLayout.transitionToStart()
    }

    private fun showRegisterView() {
        mBinding.motionLayout.addTransitionListener(object : TransitionAdapter() {
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                mBinding.motionLayout.removeTransitionListener(this)
                mBinding.viewLogin.root.visibility = View.GONE
            }

            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
            ) {
                mBinding.viewRegister.root.visibility = View.VISIBLE
            }
        })
        mBinding.motionLayout.transitionToEnd()
    }

}
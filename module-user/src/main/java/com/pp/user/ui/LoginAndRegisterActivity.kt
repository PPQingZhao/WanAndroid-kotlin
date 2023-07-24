package com.pp.user.ui

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.pp.base.ThemeActivity
import com.pp.router_service.RouterPath
import com.pp.user.databinding.ActivityLoginAndRegisterBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Route(path = RouterPath.User.activity_login)
class LoginAndRegisterActivity :
    ThemeActivity<ActivityLoginAndRegisterBinding, LoginAndRegisterViewModel>() {

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, LoginAndRegisterActivity::class.java)
            val toBundle = ActivityOptions.makeSceneTransitionAnimation(activity).toBundle()
            activity.startActivity(intent, toBundle)
        }
    }

    override val mBinding: ActivityLoginAndRegisterBinding by lazy {
        ActivityLoginAndRegisterBinding.inflate(
            layoutInflater
        )
    }

    override fun getModelClazz(): Class<LoginAndRegisterViewModel> {
        return LoginAndRegisterViewModel::class.java
    }

    fun onBack(view: View) {
        onBackPressedDispatcher.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initLoginViewModel()
        initRegisterViewModel()

        initMotionLayout()
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
                    ActivityCompat.finishAfterTransition(this@LoginAndRegisterActivity)
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
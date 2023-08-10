package com.pp.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.pp.base.ThemeActivity
import com.pp.common.app.App
import com.pp.main.databinding.ActivityMainBinding
import com.pp.main.databinding.ActivityMainBindingImpl
import com.pp.router_service.RouterPath

class MainActivity : ThemeActivity<ActivityMainBinding, MainViewModel>() {

    override val mBinding: ActivityMainBinding by lazy {
        ActivityMainBindingImpl.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, MainActivity::class.java))
        }
    }

    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.getInstance().navigation.observe(this) {

            when (it) {
                RouterPath.User.fragment_login -> {
                    showLogin()
                }
                RouterPath.Main.fragment_main,
                RouterPath.User.fragment_user,
                -> {
                    showMain()
                }
            }
        }
    }

    @SuppressLint("CommitTransaction")
    private fun showLogin() {
        var loginFragment =
            supportFragmentManager.findFragmentByTag(RouterPath.User.fragment_login)

        if (null == loginFragment) {
            loginFragment = ARouter.getInstance()
                .build(RouterPath.User.fragment_login).navigation() as Fragment
        }

        val oldFragment = curFragment
        supportFragmentManager.beginTransaction()
            .run {
                if (null != oldFragment) {
                    hide(oldFragment)
                }
                add(R.id.main_container, loginFragment, RouterPath.User.fragment_login)
                show(loginFragment)
                commitNow()
            }
        curFragment = loginFragment
    }

    private var curFragment: Fragment? = null
    private fun showMain() {
        var mainFragment = supportFragmentManager.findFragmentByTag(RouterPath.Main.fragment_main)
        if (null == mainFragment) {
            mainFragment = MainFragment()
        }

        supportFragmentManager.beginTransaction()
            .run {
                replace(R.id.main_container, mainFragment, RouterPath.Main.fragment_main)
                show(mainFragment)
                commitNow()
            }
        curFragment = mainFragment

    }


}

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
                RouterPath.Main.fragment_main,
                RouterPath.User.fragment_user,
                -> {
                    showFragment(getMainFragment(), RouterPath.Main.fragment_main)
                }
                RouterPath.User.fragment_login -> {
                    showFragment(getLoginFragment(), RouterPath.User.fragment_login)
                }
            }
        }
    }

    private fun getMainFragment(): Fragment {
        var mainFragment =
            supportFragmentManager.findFragmentByTag(RouterPath.Main.fragment_main)
        if (null == mainFragment) {
            mainFragment = MainFragment()
        }
        return mainFragment
    }

    private fun getLoginFragment(): Fragment {
        var loginFragment =
            supportFragmentManager.findFragmentByTag(RouterPath.User.fragment_login)

        if (null == loginFragment) {
            loginFragment = ARouter.getInstance()
                .build(RouterPath.User.fragment_login).navigation() as Fragment
        }
        return loginFragment
    }

    private var curFragment: Fragment? = null

    @SuppressLint("CommitTransaction")
    private fun showFragment(fragment: Fragment, tag: String) {

        val oldFragment = curFragment
        supportFragmentManager.beginTransaction()
            .run {
                oldFragment?.run {
                    hide(oldFragment)
                }

                if (!fragment.isAdded) {
                    add(R.id.main_container, fragment, tag)
                }
                show(fragment)
                commitNow()
            }
        curFragment = fragment
    }


}

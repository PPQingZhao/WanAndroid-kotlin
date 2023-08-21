package com.pp.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.alibaba.android.arouter.launcher.ARouter
import com.google.android.material.transition.MaterialSharedAxis
import com.pp.base.ThemeActivity
import com.pp.common.app.App
import com.pp.common.browser.CommonWebViewFragment
import com.pp.common.materialSharedAxis
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

            when (it.first) {
                RouterPath.User.fragment_user,
                RouterPath.Main.fragment_main,
                -> {
                    showFragment(getMainFragment(), RouterPath.Main.fragment_main)
                }
                RouterPath.User.fragment_login -> {
                    getMainFragment().let { f ->
                        f.exitTransition = materialSharedAxis(MaterialSharedAxis.X, true)
                        f.enterTransition = materialSharedAxis(MaterialSharedAxis.X, false)
                    }
                    getLoginFragment().let {
                        showFragment(it, RouterPath.User.fragment_login)
                        toRemoveFragment = it
                    }
                }
                RouterPath.Web.fragment_web -> {
                    getMainFragment().let { f ->
//                        f.exitTransition = materialSharedAxis(MaterialSharedAxis.X, true)
//                        f.enterTransition = materialSharedAxis(MaterialSharedAxis.X, false)
                    }
                    getWebFragment().let { f ->
                        val secondArg = it.second
                        if (secondArg is Bundle) {
                            f.arguments = secondArg
                        }
                        showFragment(f, RouterPath.Web.fragment_web)
                        toRemoveFragment = f
                    }
                }
            }
        }
    }

    private fun getWebFragment(): Fragment {
        var webFragment =
            supportFragmentManager.findFragmentByTag(RouterPath.Web.fragment_web)
        if (null == webFragment) {
            webFragment = CommonWebViewFragment()
        }
        return webFragment
    }

    private var curFragment: Fragment? = null
    private var toRemoveFragment: Fragment? = null

    @SuppressLint("CommitTransaction")
    private fun showFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction().let { transition ->
            val oldFragment = curFragment
            oldFragment?.let {
                transition.hide(it)
                transition.setMaxLifecycle(it, Lifecycle.State.STARTED)
            }

            toRemoveFragment?.let {
                transition.remove(toRemoveFragment!!)
                toRemoveFragment = null
            }

            fragment.let {
                if (!it.isAdded) {
                    transition.add(R.id.container, it, tag)
                }

                transition.show(it)
                    .setMaxLifecycle(it, Lifecycle.State.RESUMED)
                    .commitNow()
            }

            curFragment = fragment
        }
    }

    private fun getMainFragment(): Fragment {
        return supportFragmentManager.findFragmentByTag(RouterPath.Main.fragment_main)
            ?: MainFragment()
    }

    private fun getLoginFragment(): Fragment {
        var loginFragment =
            supportFragmentManager.findFragmentByTag(RouterPath.User.fragment_login)
        if (null == loginFragment) {
            loginFragment =
                ARouter.getInstance().build(RouterPath.User.fragment_login).navigation() as Fragment
        }

        return loginFragment
    }

}

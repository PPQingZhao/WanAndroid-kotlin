package com.pp.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks
import androidx.lifecycle.Lifecycle
import com.alibaba.android.arouter.launcher.ARouter
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialSharedAxis
import com.pp.base.ThemeActivity
import com.pp.common.app.App
import com.pp.common.browser.CommonWebViewFragment
import com.pp.common.constant.Constants
import com.pp.common.util.materialSharedAxis
import com.pp.common.util.ShareElementNavigation
import com.pp.common.util.materialElevationScale
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

    private val DEBUG = true
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        App.getInstance().navigation.value = RouterPath.Main.fragment_main to Any()
    }

    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.registerFragmentLifecycleCallbacks(object :
            FragmentLifecycleCallbacks() {
            override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
                if (null != f.view?.parent && !f.isHidden) {
                    showingFragment = f
                }

                if (DEBUG) {
                    Log.e("MainActivity", "showingFragment: $showingFragment")
                }
            }
        }, false)

        App.getInstance().navigation.observe(this) {

            when (it.first) {
                RouterPath.Main.fragment_main -> {
                    showFragment(
                        getMainFragment(),
                        RouterPath.Main.fragment_main,
                        addToBackStack = false
                    )
                }
                RouterPath.User.fragment_login -> {
                    getMainFragment().let { f ->
                        if (f::class.java.equals(showingFragment!!::class.java)) {
                            f.exitTransition = materialSharedAxis(MaterialSharedAxis.X, true)
                            f.reenterTransition = materialSharedAxis(MaterialSharedAxis.X, false)
                        }
                    }
                    showFragment(getLoginFragment(), RouterPath.User.fragment_login)
                }
                RouterPath.Web.fragment_web -> {
                    getMainFragment().let { f ->
                        if (f::class.java.equals(showingFragment!!::class.java)) {
                            f.exitTransition = materialElevationScale(false)
                            f.reenterTransition = materialElevationScale(true)
                        }
                    }
                    getWebFragment().let { f ->

                        val secondArg = it.second
                        var sharedElement: View? = null
                        if (secondArg is ShareElementNavigation) {
                            f.arguments = secondArg.bundle
                            sharedElement = secondArg.shareElement
                        }
                        showFragment(
                            f,
                            RouterPath.Web.fragment_web,
                            sharedElement,
                            true
                        )
                    }
                }
                RouterPath.Navigation.fragment_tab_system -> {

                    getMainFragment().let { f ->
                        f.exitTransition = null
                        f.reenterTransition = null
                    }

                    getTabSystemFragment().let { f ->
                        val secondArg = it.second
                        if (secondArg is Bundle) {
                            f.arguments = secondArg
                        }
                        showFragment(f, RouterPath.Navigation.fragment_tab_system)
                    }
                }
                RouterPath.User.fragment_user,
                Constants.ON_BACK_PRESSED,
                -> {
                    popBackStack()
                }
            }
        }
    }

    @SuppressLint("CommitTransaction")
    private fun popBackStack() {
        supportFragmentManager.popBackStackImmediate()
    }

    private var showingFragment: Fragment? = null

    @SuppressLint("CommitTransaction")
    private fun showFragment(
        fragment: Fragment,
        tag: String,
        sharedElement: View? = null,
        addToBackStack: Boolean = true,
    ) {
        supportFragmentManager.beginTransaction().let { transition ->
            val oldFragment = showingFragment
            showingFragment = null
            oldFragment?.let {
                transition.hide(it)
                if (oldFragment.isAdded) {
                    transition.setMaxLifecycle(it, Lifecycle.State.STARTED)
                }
            }

            fragment.let { f ->
                if (!f.isAdded) {
                    transition.add(R.id.container, f, tag)
                }

                if (null != sharedElement) {
                    transition.addSharedElement(sharedElement, sharedElement.transitionName)
                }

                if (addToBackStack) {
                    transition.addToBackStack("main")
                }
                transition
                    .show(f)
                    .setMaxLifecycle(f, Lifecycle.State.RESUMED)
                    .commit()
            }
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

    private var mWebFragment: Fragment? = null
    private fun getWebFragment(): Fragment {
        val webFragment = supportFragmentManager.findFragmentByTag(RouterPath.Web.fragment_web)
        mWebFragment = webFragment ?: CommonWebViewFragment()
        return mWebFragment!!
    }

    private fun getTabSystemFragment(): Fragment {
        var tabSystemFragment =
            supportFragmentManager.findFragmentByTag(RouterPath.Navigation.fragment_tab_system)
        if (null == tabSystemFragment) {
            tabSystemFragment =
                ARouter.getInstance().build(RouterPath.Navigation.fragment_tab_system)
                    .navigation() as Fragment
        }
        return tabSystemFragment
    }
}

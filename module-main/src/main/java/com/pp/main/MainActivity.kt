package com.pp.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.alibaba.android.arouter.launcher.ARouter
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialSharedAxis
import com.pp.base.ThemeActivity
import com.pp.common.app.App
import com.pp.common.browser.CommonWebViewFragment
import com.pp.common.constant.Constants
import com.pp.common.materialSharedAxis
import com.pp.common.util.ShareElementNavigation
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

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        App.getInstance().navigation.value = RouterPath.Main.fragment_main to Any()
    }

    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.getInstance().navigation.observe(this) {
            when (it.first) {
                RouterPath.User.fragment_user,
                RouterPath.Main.fragment_main,
                -> {
                    showFragment(
                        getMainFragment(),
                        RouterPath.Main.fragment_main,
                        addToBackStack = false
                    )
                }
                RouterPath.User.fragment_login -> {
                    getMainFragment().let { f ->
                        f.exitTransition = materialSharedAxis(MaterialSharedAxis.X, true)
                        f.enterTransition = materialSharedAxis(MaterialSharedAxis.X, false)
                    }
                    showFragment(getLoginFragment(), RouterPath.User.fragment_login)
                }
                RouterPath.Web.fragment_web -> {
                    getMainFragment().let { f ->
                        f.exitTransition = null
                        f.enterTransition = null
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
                        f.enterTransition = null
                    }
                    getTabSystemFragment().let { f ->
                        val secondArg = it.second
                        if (secondArg is Bundle) {
                            f.arguments = secondArg
                        }
                        showFragment(f, RouterPath.Navigation.fragment_tab_system)
                    }
                }
                Constants.ON_BACK_PRESSED -> {
                    popBackStack()
                }
            }
        }
    }

    @SuppressLint("CommitTransaction")
    private fun popBackStack() {
        supportFragmentManager.beginTransaction().let { transition ->
            val oldFragment = showingFragment
            oldFragment?.let { f ->
                if (f.isAdded) {
                    transition.setMaxLifecycle(f, Lifecycle.State.STARTED)
                        .commit()
                }
            }
            supportFragmentManager.popBackStackImmediate()

            supportFragmentManager.fragments.onEach { f ->
                if (f.isVisible && f.isResumed && !f.isHidden) {
                    showingFragment = f
                    return@onEach
                }
            }

        }

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
            oldFragment?.let {
                transition.hide(it)
                if (oldFragment.isAdded) {
                    transition.setMaxLifecycle(it, Lifecycle.State.STARTED)
                }
            }

            showingFragment = null
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
                transition.show(f)
                    .setMaxLifecycle(f, Lifecycle.State.RESUMED)
                    .commit()
            }

            showingFragment = fragment
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

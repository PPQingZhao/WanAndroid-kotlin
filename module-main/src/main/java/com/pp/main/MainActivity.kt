package com.pp.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.pp.base.ThemeActivity
import com.pp.base.helper.PagerFragmentHelper
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

        mBinding.viewpager2.run {
            isUserInputEnabled = false
            PagerFragmentHelper(this@MainActivity)
                .addPagers(getPagers())
                .attach(this)
        }

        App.getInstance().navigation.observe(this) {

            when (it) {
                RouterPath.Main.fragment_main,
                RouterPath.User.fragment_user,
                -> {
                    mBinding.viewpager2.currentItem = 0
                }
                RouterPath.User.fragment_login -> {
                    mBinding.viewpager2.currentItem = 1
                }
            }
        }
    }

    private fun getPagers(): List<PagerFragmentHelper.Pager> {
        return mutableListOf<PagerFragmentHelper.Pager>().apply {
            add(PagerFragmentHelper.Pager {
                MainFragment()
            })

            add(PagerFragmentHelper.Pager {
                ARouter.getInstance()
                    .build(RouterPath.User.fragment_login).navigation() as Fragment
            })
        }
    }


}

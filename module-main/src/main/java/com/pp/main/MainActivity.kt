package com.pp.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.pp.base.ThemeActivity
import com.pp.base.ThemeViewModel
import com.pp.main.databinding.ActivityMainBinding
import com.pp.router_service.RouterPath

class MainActivity : ThemeActivity<ActivityMainBinding, ThemeViewModel>() {

    override val mBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<ThemeViewModel> {
        return ThemeViewModel::class.java
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.run {
            val settingfragment =
                ARouter.getInstance().build(RouterPath.Local.fragment_theme_setting)
                    .navigation() as Fragment
            val mainFragment = MainFragment()
            beginTransaction().replace(
                R.id.fl_theme_setting,
                settingfragment,
                RouterPath.Local.fragment_theme_setting
            ).replace(
                R.id.fl_main,
                mainFragment,
                "MainFragment"
            ).commitNow()
        }

    }

    @SuppressLint("DiscouragedPrivateApi")
    fun onTheme(v: View) {
        Log.e("TAG", "onTheme")
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                Array(1) { Manifest.permission.READ_EXTERNAL_STORAGE },
                PackageManager.PERMISSION_GRANTED
            )
            return
        }

    }


}
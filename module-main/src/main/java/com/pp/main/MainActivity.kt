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
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.launcher.ARouter
import com.pp.base.ThemeActivity
import com.pp.base.ThemeViewModel
import com.pp.base.WanAndroidTheme
import com.pp.base.updateTheme
import com.pp.main.databinding.ActivityMainBinding
import com.pp.router_service.RouterPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        val fragment = ARouter.getInstance().build(RouterPath.Local.fragment_theme_setting)
            .navigation() as Fragment

        supportFragmentManager.run {
            beginTransaction().replace(
                R.id.fl_theme_setting,
                fragment,
                RouterPath.Local.fragment_theme_setting
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

        skin()
    }

    var flag = 0

    private fun skin() {
        lifecycleScope.launch(Dispatchers.IO) {
            updateTheme(
                when (flag % 3) {
                    0 -> WanAndroidTheme.Blue
                    1 -> WanAndroidTheme.Black
                    else -> WanAndroidTheme.Default
                }
            )
            flag++
        }
    }

}
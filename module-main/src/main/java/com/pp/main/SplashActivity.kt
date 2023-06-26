package com.pp.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.pp.base.ThemeActivity
import com.pp.base.ThemeViewModel
import com.pp.main.databinding.ActivitySplashBinding

/**
 * 启动页
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : ThemeActivity<ActivitySplashBinding, ThemeViewModel>() {

    override val mBinding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<ThemeViewModel> {
        return ThemeViewModel::class.java
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireLightStatusBar(false)

        Handler(Looper.myLooper()!!).postDelayed({
            MainActivity.start(this)
            finish()
        }, 500)
    }
}
package com.pp.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.pp.base.ThemeActivity
import com.pp.base.ThemeViewModel
import com.pp.common.router.MultiRouterFragmentActivity.Companion.startMultiRouterFragmentActivity
import com.pp.main.databinding.ActivitySplashBinding
import com.pp.router_service.RouterPath

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

    private val resultPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it[Manifest.permission.READ_EXTERNAL_STORAGE] == true) {
                startIntent()
            } else {
                finish()
            }
        }

    private fun startIntent() {
        Handler(Looper.myLooper()!!).postDelayed({

            startMultiRouterFragmentActivity(this, RouterPath.Main.fragment_main)
            finish()
        }, 500)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            startIntent()
        }
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            resultPermission.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
        }
    }
}
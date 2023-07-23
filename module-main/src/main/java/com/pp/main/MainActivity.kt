package com.pp.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.pp.base.ThemeActivity
import com.pp.base.ThemeViewModel
import com.pp.main.databinding.ActivityMainBinding
import com.pp.theme.AppDynamicTheme
import com.pp.theme.DynamicTheme
import com.pp.theme.DynamicThemeManager
import com.pp.theme.extension.init
import com.pp.theme.factory.SkinThemeFactory
import java.io.File

class MainActivity : ThemeActivity<ActivityMainBinding, ThemeViewModel>() {

    override val mBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val mAppDynamicTheme = AppDynamicTheme().apply {
        init(this@MainActivity)
    }

    override fun getDynamicTheme(): DynamicTheme {
        return mAppDynamicTheme
    }

    override fun getModelClazz(): Class<ThemeViewModel> {
        return ThemeViewModel::class.java
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, MainActivity::class.java))
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

    var skin = "skinBlack.skin"

    @SuppressLint("DiscouragedPrivateApi")
    private fun skin() {

        DynamicThemeManager.create(mAppDynamicTheme)
            .run {

                skin = if (skin == "skinBlack.skin") {
                    "skinBlue.skin"
                } else {
                    "skinBlack.skin"
                }

                val skinPath =
                    Environment.getExternalStorageDirectory().absolutePath +
                            File.separator + "wanandroid" +
                            File.separator + "theme" +
                            File.separator + "skin" +
                            File.separator + skin
                SkinThemeFactory(
                    skinPath,
                    resources.displayMetrics,
                    resources.configuration,
                    "Theme.Dynamic",
                    "com.pp.skin"
                ).create()?.run {
                    applyTheme(this)
                }
            }

    }

}
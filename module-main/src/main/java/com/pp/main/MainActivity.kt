package com.pp.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.launcher.ARouter
import com.pp.base.ThemeActivity
import com.pp.base.ThemeViewModel
import com.pp.main.databinding.ActivityMainBinding
import com.pp.router_service.RouterPath
import com.pp.theme.DynamicThemeManager
import com.pp.theme.factory.SkinThemeFactory
import java.io.File

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
        ARouter.getInstance().build(RouterPath.User.activity_login).navigation()
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

        DynamicThemeManager.create(dynamicTheme!!)
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
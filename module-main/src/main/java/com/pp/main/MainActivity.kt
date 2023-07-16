package com.pp.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.pp.base.ThemeActivity
import com.pp.base.ThemeViewModel
import com.pp.main.databinding.ActivityMainBinding
import java.io.File
import java.security.Permission

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

    @SuppressLint("DiscouragedPrivateApi")
    fun onTheme(v: View) {
        Log.e("TAG", "onTheme")
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                Array(1, { Manifest.permission.READ_EXTERNAL_STORAGE }),
                0
            )
            return
        }

        skin()
    }

    fun skin() {
        val skinPath =
            Environment.getExternalStorageDirectory().absolutePath +
                    File.separator + "wanandroid" +
                    File.separator + "theme" +
                    File.separator + "skin" +
                    File.separator + "skin-blue.skin"
        Log.e("TAG", "skin: " + File(skinPath).exists())
        try {
            val assetManager = AssetManager::class.java.newInstance()
            val addAssetPathMethod =
                AssetManager::class.java.getDeclaredMethod("addAssetPath", String::class.java)
            addAssetPathMethod.isAccessible = true
            addAssetPathMethod.invoke(assetManager, skinPath)

            val skinResources =
                Resources(assetManager, resources.displayMetrics, resources.configuration)

            val themeId =
                skinResources.getIdentifier("Theme.Dynamic", "style", "com.pp.skin_blue")
            Log.e("TAG","themeId: $themeId")
            val newTheme = skinResources.newTheme()
            newTheme.applyStyle(themeId, true)
            mThemeViewModel.setTheme(newTheme)
        } catch (e: RuntimeException) {
            e.printStackTrace()
        }

    }

}
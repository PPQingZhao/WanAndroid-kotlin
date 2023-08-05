package com.pp.main

import android.R
import android.app.Application
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.pp.base.ThemeViewModel

class MainViewModel(app: Application) : ThemeViewModel(app) {
    val iconHome = "textColor"
    val textColor = R.attr.colorAccent

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Log.e("TAG", "iconHome: $iconHome")
    }
}
package com.pp.main

import android.app.Activity
import android.content.Intent
import com.pp.base.ThemeActivity
import com.pp.main.databinding.ActivityMainBinding
import com.pp.main.databinding.ActivityMainBindingImpl

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


}

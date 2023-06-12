package com.pp.wanandroid

import android.view.LayoutInflater
import com.pp.base.ThemeActivity
import com.pp.base.ThemeViewModel
import com.pp.wanandroid.databinding.ActivityMainBinding

class MainActivity : ThemeActivity<ActivityMainBinding, ThemeViewModel>() {

    override val mBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(LayoutInflater.from(this))
    }

    override fun getModelClazz(): Class<ThemeViewModel> {
        return ThemeViewModel::class.java
    }
}
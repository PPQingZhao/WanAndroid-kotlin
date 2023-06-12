package com.pp.wanandroid

import android.view.LayoutInflater
import com.pp.library_base.base.ThemeActivity
import com.pp.library_base.base.ThemeViewModel
import com.pp.wanandroid.databinding.ActivityMainBinding

class MainActivity : ThemeActivity<ActivityMainBinding, ThemeViewModel>() {

    override val mBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(LayoutInflater.from(this))
    }

    override fun getModelClazz(): Class<ThemeViewModel> {
        return ThemeViewModel::class.java
    }
}
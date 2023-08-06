package com.pp.home.ui

import android.os.Bundle
import com.pp.base.ThemeFragment
import com.pp.base.ThemeViewModel
import com.pp.home.databinding.FragmentTestBinding

class TestFragment(val title: String) : ThemeFragment<FragmentTestBinding, ThemeViewModel>() {
    override val mBinding: FragmentTestBinding by lazy {
        FragmentTestBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<ThemeViewModel> {
        return ThemeViewModel::class.java
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.tvTest.setText(title)
    }
}
package com.pp.main

import com.pp.base.ThemeFragment
import com.pp.main.databinding.FragmentMainBinding

class MainFragment : ThemeFragment<FragmentMainBinding, MainViewModel>() {

    override val mBinding:FragmentMainBinding by lazy {
        FragmentMainBinding.inflate(
            layoutInflater
        )
    }

    override fun getModelClazz(): Class<MainViewModel> {
        return MainViewModel::class.java
    }
}
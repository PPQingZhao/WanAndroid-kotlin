package com.pp.home.ui

import android.os.Bundle
import com.pp.base.ThemeFragment
import com.pp.home.databinding.FragmentHomeChildSquareBinding

class SquareFragment : ThemeFragment<FragmentHomeChildSquareBinding, SquareViewModel>() {
    override val mBinding: FragmentHomeChildSquareBinding by lazy {
        FragmentHomeChildSquareBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<SquareViewModel> {
        return SquareViewModel::class.java
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
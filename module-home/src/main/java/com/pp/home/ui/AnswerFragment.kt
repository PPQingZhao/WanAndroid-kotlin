package com.pp.home.ui

import android.os.Bundle
import com.pp.base.ThemeFragment
import com.pp.home.databinding.FragmentHomeChildAnswerBinding

class AnswerFragment : ThemeFragment<FragmentHomeChildAnswerBinding, AnswerViewModel>() {
    override val mBinding: FragmentHomeChildAnswerBinding by lazy {
        FragmentHomeChildAnswerBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<AnswerViewModel> {
        return AnswerViewModel::class.java
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
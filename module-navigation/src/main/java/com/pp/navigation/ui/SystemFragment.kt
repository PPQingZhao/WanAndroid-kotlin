package com.pp.navigation.ui

import com.pp.base.ThemeFragment
import com.pp.navigation.databinding.FragmentRealnavigationBinding
import com.pp.navigation.databinding.FragmentSystemBinding

class SystemFragment private constructor() :
    ThemeFragment<FragmentSystemBinding, SystemViewModel>() {
    override val mBinding: FragmentSystemBinding by lazy {
        FragmentSystemBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<SystemViewModel> {
        return SystemViewModel::class.java
    }

    companion object {
        fun newInstance(): SystemFragment {
            return SystemFragment()
        }
    }

}
package com.pp.navigation.ui

import com.pp.base.ThemeFragment
import com.pp.navigation.databinding.FragmentNavigationBinding
import com.pp.navigation.databinding.FragmentRealnavigationBinding
import com.pp.navigation.databinding.FragmentTutorialBinding

class TutorialFragment private constructor() :
    ThemeFragment<FragmentTutorialBinding, TutorialViewModel>() {
    override val mBinding: FragmentTutorialBinding by lazy {
        FragmentTutorialBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<TutorialViewModel> {
        return TutorialViewModel::class.java
    }

    companion object {
        fun newInstance(): TutorialFragment {
            return TutorialFragment()
        }
    }

}
package com.pp.project.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnAttach
import com.pp.base.ThemeFragment
import com.pp.project.databinding.FragmentCidprojectBinding
import com.pp.ui.utils.StateView
import com.pp.ui.utils.attachStateView

class CidProjectFragment private constructor() :
    ThemeFragment<FragmentCidprojectBinding, CidProjectViewModel>() {

    companion object {
        const val PROJECT_CID = "cid"

        fun newInstance(cid: Int): CidProjectFragment {
            return CidProjectFragment().apply {
                arguments = Bundle().also { it.putInt(PROJECT_CID, cid) }
            }
        }
    }

    override val mBinding: FragmentCidprojectBinding by lazy {
        FragmentCidprojectBinding.inflate(
            layoutInflater
        )
    }

    override fun getModelClazz(): Class<CidProjectViewModel> {
        return CidProjectViewModel::class.java
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val cid = it.getInt(PROJECT_CID)
            mViewModel.setCid(cid)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.refreshLayout.doOnAttach {
            StateView.DefaultBuilder(mBinding.refreshLayout, mViewModel.mTheme, viewLifecycleOwner)
                .setOnRetry {
                    mViewModel.refresh()
                }
                .build()
                .also {
                    mViewModel.mAdapter.attachStateView(it)
                }
        }
    }

}
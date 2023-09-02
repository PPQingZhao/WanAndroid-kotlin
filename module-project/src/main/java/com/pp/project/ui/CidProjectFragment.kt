package com.pp.project.ui

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pp.base.ThemeFragment
import com.pp.common.paging.articleDifferCallback
import com.pp.common.paging.itemProjectArticleBindItemType
import com.pp.project.databinding.FragmentCidprojectBinding
import com.pp.ui.adapter.BindingPagingDataAdapter
import com.pp.ui.utils.setPagingAdapter
import kotlinx.coroutines.launch

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

    private suspend fun initPagingList() {
        val adapter = BindingPagingDataAdapter.RecyclerViewBindingAdapterImpl(
            bindingItemType = itemProjectArticleBindItemType(
                inflater = layoutInflater,
                theme = mViewModel.mTheme
            ),
            diffCallback = articleDifferCallback
        )

        mBinding.pageList.layoutManager = LinearLayoutManager(requireContext())
        mBinding.pageList.setPagingAdapter(
            viewLifecycleOwner,
            lifecycleScope,
            mViewModel.getPageData(),
            adapter
        )
    }

    override fun onFirstResume() {
        super.onFirstResume()

        lifecycleScope.launch {
            initPagingList()
        }
    }
}
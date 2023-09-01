package com.pp.navigation.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pp.base.ThemeFragment
import com.pp.common.http.wanandroid.bean.ArticleListBean
import com.pp.common.model.ItemArticleListTextViewModel
import com.pp.common.model.ItemSelectedModel
import com.pp.common.paging.itemArticleListChildFlexBoxBindItemType
import com.pp.common.paging.itemArticleText3BindItemType
import com.pp.navigation.databinding.FragmentSystemBinding
import com.pp.ui.adapter.RecyclerViewBindingAdapter
import com.pp.ui.databinding.ItemText3Binding
import com.pp.ui.viewModel.ItemDataViewModel
import com.pp.ui.viewModel.ItemTextViewModel
import com.pp.ui.viewModel.OnItemListener
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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

    override fun onFirstResume() {

        lifecycleScope.launch {
            mViewModel.systemList.collectLatest { it ->
                mAdapter.setDataList(it)
                mArticleListAdapter.setDataList(it)
            }
        }

        mViewModel.getSystemList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerview()
    }

    private val selectedItem: ItemSelectedModel<ArticleListBean, ItemTextViewModel<ArticleListBean>> =
        ItemSelectedModel()

    private val mOnItemListener = object :
        OnItemListener<ItemDataViewModel<ArticleListBean>> {
        override fun onItemClick(
            view: View,
            item: ItemDataViewModel<ArticleListBean>,
        ): Boolean {
            selectedItem.selectedItem(item as ItemTextViewModel<ArticleListBean>)
            return true
        }
    }
    private val mAdapter by lazy {
        RecyclerViewBindingAdapter.RecyclerViewBindingAdapterImpl(
            itemArticleText3BindItemType(
                inflater = layoutInflater,
                theme = mViewModel.mTheme,
                onBindItemViewModel = { _, itemViewModel, position ->
                    if (selectedItem.getSelectedItem() == null && position == 0) {
                        selectedItem.selectedItem(itemViewModel)
                    }
                    itemViewModel.setOnItemListener(mOnItemListener)
                }
            )
        )
    }

    private val mArticleListAdapter by lazy {
        RecyclerViewBindingAdapter.RecyclerViewBindingAdapterImpl(
            itemArticleListChildFlexBoxBindItemType(
                layoutInflater,
                mViewModel.mTheme
            )
        )
    }

    private fun initRecyclerview() {
        mBinding.systemRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL).let {
            mBinding.systemRecyclerview.addItemDecoration(it)
        }
        mBinding.systemRecyclerview.adapter = mAdapter

        mBinding.articleListRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        mBinding.articleListRecyclerview.adapter = mArticleListAdapter

        selectedItem.observerSelectedItem(
            viewLifecycleOwner
        ) { item ->
            item?.run {
                lifecycleScope.launch {
                    mViewModel.systemList.collectLatest {
                        val pos = it.indexOf(item.data as Any)
                        (mBinding.articleListRecyclerview.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                            pos,
                            0
                        )
                    }
                    cancel()
                }
            }
        }

    }
}
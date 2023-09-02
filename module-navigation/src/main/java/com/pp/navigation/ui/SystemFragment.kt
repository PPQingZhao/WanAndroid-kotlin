package com.pp.navigation.ui

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexboxLayoutManager
import com.pp.base.ThemeFragment
import com.pp.common.http.wanandroid.bean.ArticleListBean
import com.pp.common.model.ItemSelectedModel
import com.pp.common.paging.*
import com.pp.navigation.databinding.FragmentSystemBinding
import com.pp.ui.adapter.MultiRecyclerViewBindingAdapter
import com.pp.ui.adapter.RecyclerViewBindingAdapter
import com.pp.ui.adapter.ViewDataBindingItemType
import com.pp.ui.viewModel.ItemDataViewModel
import com.pp.ui.viewModel.ItemTextViewModel
import com.pp.ui.viewModel.OnItemListener
import kotlinx.coroutines.async
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
            async {
                mViewModel.systemList.collectLatest { it ->
                    mAdapter.setDataList(it)
                }
            }

            async {
                mViewModel.articleList.collectLatest {
                    mBinding.articleListRecyclerview.setItemViewCacheSize(it.size)
                    mArticleListAdapter.setDataList(it)
                }
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
            itemText3ArticleBindItemType(
                inflater = layoutInflater,
                theme = mViewModel.mTheme,
                onBindItemViewModel = { _, viewModel, position ->
                    if (selectedItem.getSelectedItem() == null && position == 0) {
                        selectedItem.selectedItem(viewModel)
                    }
                    viewModel.setOnItemListener(mOnItemListener)
                }
            )
        )
    }

    private val mArticleListAdapter by lazy {
        val type_parent_article_list = 0
        val type_article_list = 1
        MultiRecyclerViewBindingAdapter(getItemViewType = {
            if (it is ArticleListBean && it.parentChapterId == 0) {
                type_parent_article_list
            } else {
                type_article_list
            }
        }).apply {

            itemText1ArticleListBindItemType(
                type_parent_article_list,
                inflater = layoutInflater,
                mViewModel.mTheme
            ).let {
                addBindingItem(it as ViewDataBindingItemType<ViewDataBinding, Any?, Any>)
            }

            itemText2ArticleListBindItemType(
                type_article_list,
                layoutInflater,
                mViewModel.mTheme
            ).let {
                addBindingItem(it as ViewDataBindingItemType<ViewDataBinding, Any?, Any>)
            }
        }

    }

    private fun initRecyclerview() {
        mBinding.systemRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL).let {
            mBinding.systemRecyclerview.addItemDecoration(it)
        }
        mBinding.systemRecyclerview.adapter = mAdapter

        mBinding.articleListRecyclerview.layoutManager =
            FlexboxLayoutManager(requireContext()).apply {
                isAutoMeasureEnabled = false
            }
        mBinding.articleListRecyclerview.adapter = mArticleListAdapter

        selectedItem.observerSelectedItem(
            viewLifecycleOwner
        ) { item ->
            item?.run {
                lifecycleScope.launch {
                    mViewModel.articleList.collectLatest {
                        val pos = it.indexOf(item.data as Any)
                        mBinding.articleListRecyclerview.scrollToPosition(pos)

                    }
                    cancel()
                }
            }
        }

    }
}
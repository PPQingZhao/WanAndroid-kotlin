package com.pp.navigation.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import com.pp.base.ThemeFragment
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.ArticleListBean
import com.pp.common.model.ItemArticleListTextViewModel
import com.pp.common.model.ItemArticleTextViewModel
import com.pp.common.model.ItemSelectedModel
import com.pp.common.paging.itemArticleListTextBindItemType
import com.pp.common.paging.itemArticleText2BindItemType
import com.pp.navigation.databinding.FragmentRealnavigationBinding
import com.pp.ui.adapter.DefaultViewDataBindingItemType
import com.pp.ui.adapter.RecyclerViewBindingAdapter
import com.pp.ui.databinding.ItemFlexboxTextBinding
import com.pp.ui.databinding.ItemText2Binding
import com.pp.ui.viewModel.ItemDataViewModel
import com.pp.ui.viewModel.ItemTextViewModel
import com.pp.ui.viewModel.OnItemListener
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NavigationRealFragment private constructor() :
    ThemeFragment<FragmentRealnavigationBinding, NavigationRealViewModel>() {
    override val mBinding: FragmentRealnavigationBinding by lazy {
        FragmentRealnavigationBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<NavigationRealViewModel> {
        return NavigationRealViewModel::class.java
    }

    companion object {
        fun newInstance(): NavigationRealFragment {
            return NavigationRealFragment()
        }
    }

    private val selectedItem: ItemSelectedModel<ArticleListBean, ItemTextViewModel<ArticleListBean>> =
        ItemSelectedModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedItem.observerSelectedItem(
            viewLifecycleOwner
        ) { item ->
            item?.run {
                lifecycleScope.launch {
                    mViewModel.navigation.collectLatest {
                        val pos = it.indexOf(item.data as Any)
//                        Log.e("TAG", "pos: $pos")
                        (mBinding.articleRecyclerview.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                            pos,
                            0
                        )
                    }
                    cancel()
                }
            }
        }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mBinding.cidRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        mBinding.cidRecyclerview.adapter = cidAdapter

        mBinding.articleRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        mBinding.articleRecyclerview.adapter = articleAdapter
    }

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

    private val cidAdapter by lazy {
        RecyclerViewBindingAdapter.RecyclerViewBindingAdapterImpl(
            itemArticleListTextBindItemType(
                layoutInflater,
                mViewModel.mTheme
            ) { _, viewModel, position ->
                if (selectedItem.getSelectedItem() == null && position == 0) {
                    selectedItem.selectedItem(viewModel)
                }
                viewModel.setOnItemListener(mOnItemListener)
            }
        )
    }


    private val articleAdapter by lazy {

        /* RecyclerViewBindingAdapter.RecyclerViewBindingAdapterImpl(
             itemArticleFlexBoxBindItemType(
                 layoutInflater,
                 mViewModel.mTheme
             )
         )*/

        val type_flexbox = 111
        val recycledViewPool =
            RecyclerView.RecycledViewPool().apply { setMaxRecycledViews(type_flexbox, 100) }

        RecyclerViewBindingAdapter.RecyclerViewBindingAdapterImpl(
            DefaultViewDataBindingItemType<ItemFlexboxTextBinding, ItemArticleListTextViewModel, ArticleListBean>(
                createBinding = {
                    Log.e("TAG", "aaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                    ItemFlexboxTextBinding.inflate(layoutInflater, it, false).apply {
                        recyclerview.setItemViewCacheSize(6)
                        recyclerview.setRecycledViewPool(recycledViewPool)
                        recyclerview.layoutManager = FlexboxLayoutManager(context)
                        recyclerview.adapter =
                            RecyclerViewBindingAdapter.RecyclerViewBindingAdapterImpl(
                                itemArticleText2BindItemType(0, layoutInflater, mViewModel.mTheme),
                                getItemViewType = { type_flexbox }
                            )
                    }
                },
                onBindItemViewModel = { bind, item, position, cachedItemModel ->
                    (bind.recyclerview.adapter as RecyclerViewBindingAdapter.RecyclerViewBindingAdapterImpl<ItemText2Binding, ItemArticleTextViewModel, ArticleBean>)
                        .setDataList(item?.articles ?: emptyList())

                    Log.e(
                        "TAG",
                        "bbbbbbbbbbbbbbbbbbbbbbb size: ${
                            recycledViewPool.getRecycledViewCount(type_flexbox)
                        }"
                    )
                    if (cachedItemModel is ItemArticleListTextViewModel) {
                        cachedItemModel.apply { data = item }
                    } else {
                        ItemArticleListTextViewModel(item, mViewModel.mTheme)
                    }
                })
        )
    }

    override fun onFirstResume() {
        lifecycleScope.launch {
            async {
                mViewModel.navigation.collectLatest {
                    cidAdapter.setDataList(it)
                    articleAdapter.setDataList(it)
                }
            }

//            async {
//                mViewModel.articles.collectLatest {
//                }
//            }
        }
        mViewModel.getNavigation()
    }


}
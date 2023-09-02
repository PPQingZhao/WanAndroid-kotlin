package com.pp.navigation.ui

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexboxLayoutManager
import com.pp.base.ThemeFragment
import com.pp.common.http.wanandroid.bean.ArticleListBean
import com.pp.common.model.ItemSelectedModel
import com.pp.common.paging.itemText1ArticleListBindItemType
import com.pp.common.paging.itemTextArticleListBindItemType
import com.pp.common.paging.itemArticleText2BindItemType
import com.pp.navigation.databinding.FragmentRealnavigationBinding
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
                    mViewModel.articles.collectLatest {
                        val pos = it.indexOf(item.data as Any)
                        mBinding.articleRecyclerview.scrollToPosition(pos)

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

        mBinding.articleRecyclerview.layoutManager = FlexboxLayoutManager(requireContext()).apply {
            isAutoMeasureEnabled = false
        }
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
            itemTextArticleListBindItemType(
                inflater = layoutInflater,
                theme = mViewModel.mTheme
            ) { _, viewModel, position ->
                if (selectedItem.getSelectedItem() == null && position == 0) {
                    selectedItem.selectedItem(viewModel)
                }
                viewModel.setOnItemListener(mOnItemListener)
            }
        )
    }

    private val articleAdapter by lazy {

        val type_article_list = 0
        val type_article = 1
        MultiRecyclerViewBindingAdapter(getItemViewType = {
            if (it is ArticleListBean) {
                type_article_list
            } else {
                type_article
            }
        }).apply {
            itemText1ArticleListBindItemType(
                type_article_list, layoutInflater, mViewModel.mTheme
            ).let {
                addBindingItem(it as ViewDataBindingItemType<ViewDataBinding, Any?, Any>)
            }

            itemArticleText2BindItemType(type_article, layoutInflater, mViewModel.mTheme).let {
                addBindingItem(it as ViewDataBindingItemType<ViewDataBinding, Any?, Any>)
            }
        }

    }

    override fun onFirstResume() {
        lifecycleScope.launch {
            async {
                mViewModel.navigation.collectLatest {
                    cidAdapter.setDataList(it)
                }
            }

            async {
                mViewModel.articles.collectLatest {
                    articleAdapter.setDataList(it)
                    mBinding.articleRecyclerview.setItemViewCacheSize(it.size)
                }
            }
        }
        mViewModel.getNavigation()
    }


}
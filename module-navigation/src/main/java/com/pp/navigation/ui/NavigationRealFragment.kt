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
import com.pp.common.paging.itemText1ArticleListBinder
import com.pp.common.paging.itemText2ArticleBinder
import com.pp.common.paging.itemTextArticleListBinder
import com.pp.navigation.databinding.FragmentRealnavigationBinding
import com.pp.ui.R
import com.pp.ui.adapter.ItemViewModelBinder
import com.pp.ui.adapter.RecyclerViewBindingAdapter2
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

        RecyclerViewBindingAdapter2<ArticleListBean>(getItemLayoutRes = { R.layout.item_text })
            .apply {
                itemTextArticleListBinder(
                    onItemListener = mOnItemListener,
                    theme = mViewModel.mTheme,
                    onBindViewModel = { _, data, viewModel, position ->
                        if (selectedItem.getSelectedItem() == null && position == 0) {
                            selectedItem.selectedItem(viewModel)
                        }
                        false
                    }).also {
                    addItemViewModelBinder(it)
                }
            }
    }

    private val articleAdapter by lazy {
        RecyclerViewBindingAdapter2<Any>(getItemLayoutRes = {
            if (it is ArticleListBean) {
                R.layout.item_text1
            } else {
                R.layout.item_text2
            }
        }).apply {
            itemText1ArticleListBinder(
                theme = mViewModel.mTheme
            ).also {
                addItemViewModelBinder(it as ItemViewModelBinder<ViewDataBinding, Any>)
            }

            itemText2ArticleBinder(
                theme = mViewModel.mTheme,
            ).also {
                addItemViewModelBinder(it as ItemViewModelBinder<ViewDataBinding, Any>)
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
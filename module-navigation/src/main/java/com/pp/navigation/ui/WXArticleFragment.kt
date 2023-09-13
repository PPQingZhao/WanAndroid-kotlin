package com.pp.navigation.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pp.base.ThemeFragment
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.ArticleListBean
import com.pp.common.model.ItemSelectedModel
import com.pp.common.paging.articleDifferCallback
import com.pp.common.paging.itemText3ArticleBinder
import com.pp.navigation.databinding.FragmentWxarticleBinding
import com.pp.ui.R
import com.pp.ui.adapter.BindingPagingDataAdapter
import com.pp.ui.adapter.RecyclerViewBindingAdapter
import com.pp.ui.utils.setPagingAdapter
import com.pp.ui.viewModel.ItemDataViewModel
import com.pp.ui.viewModel.ItemTextViewModel
import com.pp.ui.viewModel.OnItemListener
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WXArticleFragment private constructor() :
    ThemeFragment<FragmentWxarticleBinding, WXArticleViewModel>() {
    override val mBinding: FragmentWxarticleBinding by lazy {
        FragmentWxarticleBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<WXArticleViewModel> {
        return WXArticleViewModel::class.java
    }

    companion object {
        fun newInstance(): WXArticleFragment {
            return WXArticleFragment()
        }
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


        RecyclerViewBindingAdapter<ArticleListBean>(getItemLayoutRes = { R.layout.item_text3 })
            .apply {
                itemText3ArticleBinder(
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

    private fun initRecyclerview() {
        mBinding.authorRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL).let {
            mBinding.authorRecyclerview.addItemDecoration(it)
        }
        mBinding.authorRecyclerview.adapter = mAdapter

        mBinding.wxarticleRecyclerview.layoutManager = LinearLayoutManager(requireContext())


        selectedItem.observerSelectedItem(
            viewLifecycleOwner
        ) {
            it?.run {
                mBinding.wxarticleRecyclerview.setPagingAdapter(
                    viewLifecycleOwner,
                    lifecycleScope,
                    mViewModel.getWXArticle(this.data?.id ?: 0),
                    BindingPagingDataAdapter<ArticleBean>(
                        { R.layout.item_wx_article },
                        diffCallback = articleDifferCallback
                    ).apply {
                        com.pp.common.paging.itemWXArticleBinder(mViewModel.mTheme).also {
                            addItemViewModelBinder(it)
                        }
                    })
            }
        }
    }

    override fun onFirstResume() {
        lifecycleScope.launch {
            mViewModel.wxArticleList.collectLatest {
                mAdapter.setDataList(it)
            }

        }
        mViewModel.getWXArticleList()
    }


}
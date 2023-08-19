package com.pp.home.ui

import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pp.base.ThemeFragment
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.home.databinding.FragmentArticleListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class ArticleListFragment<VM : ArticleListViewModel> :
    ThemeFragment<FragmentArticleListBinding, VM>() {

    override val mBinding: FragmentArticleListBinding by lazy {
        FragmentArticleListBinding.inflate(layoutInflater)
    }


    fun setAdapter(adapter: PagingDataAdapter<ArticleBean, out RecyclerView.ViewHolder>) {
        mBinding.recyclerview.let {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
        }
        lifecycleScope.launch(Dispatchers.IO) {
            mViewModel.getPageData().collect {
                adapter.submitData(lifecycle, it)
            }
        }
    }


}
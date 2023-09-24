package com.pp.navigation.ui

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.pp.base.ThemeViewModel
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.paging.articleDifferCallback
import com.pp.common.paging.collectedListener
import com.pp.common.paging.itemWXArticleBinder
import com.pp.common.repository.UserRepository
import com.pp.common.repository.getPreferenceUserWhenResume
import com.pp.navigation.repository.SystemRepository
import com.pp.ui.R
import com.pp.ui.adapter.BindingPagingDataAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ArticleListViewModel(app: Application) : ThemeViewModel(app) {

    private var mCid = 0

    fun setCid(cid: Int) {
        mCid = cid
    }


    fun getSystemArticle(): Flow<PagingData<ArticleBean>> {
        return SystemRepository.getSystemArticle(mCid)
    }

    fun refresh() {
        mWXArticleAdapter.refresh()
    }

    val mWXArticleAdapter by lazy {
        BindingPagingDataAdapter<ArticleBean>(
            { R.layout.item_wx_article },
            diffCallback = articleDifferCallback
        ).apply {
            itemWXArticleBinder(mTheme, viewModelScope).also {
                addItemViewModelBinder(it)
            }

            collectedListener(viewModelScope)
        }
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        UserRepository.getPreferenceUserWhenResume(owner){
            refresh()
        }
    }

    override fun onFirstResume(owner: LifecycleOwner) {
        viewModelScope.launch(Dispatchers.IO) {
            getSystemArticle().collectLatest {
                mWXArticleAdapter.setPagingData(viewModelScope, it)
            }
        }
    }

}
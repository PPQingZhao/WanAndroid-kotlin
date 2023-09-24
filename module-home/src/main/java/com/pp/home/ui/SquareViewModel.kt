package com.pp.home.ui

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.pp.base.ThemeViewModel
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.paging.itemArticlePagingAdapter
import com.pp.common.repository.UserRepository
import com.pp.common.repository.getPreferenceUserWhenResume
import com.pp.home.repository.HomeRepository
import com.pp.home.repository.SquareRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SquareViewModel(app: Application) : ThemeViewModel(app) {
    private fun getPageData(): Flow<PagingData<ArticleBean>> {
        return SquareRepository.getPageData()
    }

    fun refresh() {
        mArticleAdapter.refresh()
    }

    val mArticleAdapter by lazy {
        itemArticlePagingAdapter(mTheme, viewModelScope)
    }

    override fun onCreate(owner: LifecycleOwner) {
        UserRepository.getPreferenceUserWhenResume(owner) {
            refresh()
        }
    }

    override fun onFirstResume(owner: LifecycleOwner) {
        viewModelScope.launch(Dispatchers.IO) {
            getPageData().collectLatest {
                mArticleAdapter.setPagingData(viewModelScope, it)
            }
        }
    }
}
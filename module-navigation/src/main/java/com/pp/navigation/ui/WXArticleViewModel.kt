package com.pp.navigation.ui

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.pp.base.ThemeViewModel
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.ArticleListBean
import com.pp.navigation.repository.NavigationRepository
import com.pp.navigation.repository.WXArticleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class WXArticleViewModel(app: Application) : ThemeViewModel(app) {


    private val _wxArticleList = MutableSharedFlow<List<ArticleListBean>>()
    val wxArticleList = _wxArticleList.asSharedFlow()
    fun getWXArticleList() {
        viewModelScope.launch {
            WXArticleRepository.getWXArticleList().data.let {
                _wxArticleList.emit(it ?: emptyList())
            }
        }
    }

    fun getWXArticle(id : Int): Flow<PagingData<ArticleBean>> {
        return WXArticleRepository.getWXArticle(id)
    }
}
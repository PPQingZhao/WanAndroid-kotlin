package com.pp.navigation.ui

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.pp.base.ThemeViewModel
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.ArticleListBean
import com.pp.navigation.repository.NavigationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class NavigationRealViewModel(app: Application) : ThemeViewModel(app) {
    private val _navigation = MutableSharedFlow<List<ArticleListBean>>()
    val navigation = _navigation.asSharedFlow()

    private val _articles = MutableSharedFlow<List<ArticleBean>>()
    val articles = _articles.asSharedFlow()

    fun getNavigation() {
        viewModelScope.launch(Dispatchers.IO) {

            NavigationRepository.getNavigation().also {
                if (null == it.data) {
                    _navigation.emit(emptyList())
                    _articles.emit(emptyList())
                } else {

                    _navigation.emit(it.data!!)
                    val articleList = mutableListOf<ArticleBean>()
                    it.data!!.onEach {
//                        articleList.add(it)
                        articleList.addAll(it.articles)
                    }
                    _articles.emit(articleList)
                }
            }
        }
    }
}
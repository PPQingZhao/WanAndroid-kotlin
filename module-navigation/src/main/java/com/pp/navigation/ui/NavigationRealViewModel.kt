package com.pp.navigation.ui

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.pp.base.ThemeViewModel
import com.pp.common.http.wanandroid.bean.ArticleListBean
import com.pp.navigation.repository.NavigationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NavigationRealViewModel(app: Application) : ThemeViewModel(app) {
    private val _navigation = MutableStateFlow<List<ArticleListBean>>(emptyList())
    val navigation = _navigation.asStateFlow()

    private val _articles = MutableStateFlow<List<Any>>(emptyList())
    val articles = _articles.asStateFlow()

    fun getNavigation() {
        viewModelScope.launch(Dispatchers.IO) {

            NavigationRepository.getNavigation().also {
                if (null == it.data) {
                    _navigation.emit(emptyList())
                    _articles.emit(emptyList())
                } else {

                    _navigation.emit(it.data!!)
                    val articleList = mutableListOf<Any>()
                    it.data!!.onEach {
                        articleList.add(it)
                        it.articles?.let { it1 -> articleList.addAll(it1) }
                    }
                    _articles.emit(articleList)
                }
            }
        }
    }
}
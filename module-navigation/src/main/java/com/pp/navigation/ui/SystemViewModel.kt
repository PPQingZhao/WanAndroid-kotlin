package com.pp.navigation.ui

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.pp.base.ThemeViewModel
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.ArticleListBean
import com.pp.navigation.repository.SystemRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SystemViewModel(app: Application) : ThemeViewModel(app) {

    private val _systemList = MutableStateFlow<List<ArticleListBean>>(emptyList())
    val systemList = _systemList.asStateFlow()

    private val _articlesList = MutableStateFlow<List<Any>>(emptyList())
    val articleList = _articlesList.asStateFlow()
    fun getSystemList() {
        viewModelScope.launch {
            SystemRepository.getSystemList().let {
                if (null == it.data) {
                    return@let
                }
                _systemList.emit(it.data!!)
                val articleList = mutableListOf<Any>()
                it.data!!.onEach {
                    articleList.add(it)
                    articleList.addAll(it.children)
                }
                _articlesList.emit(articleList)

            }
        }
    }

    fun getSystemArticle(cid: Int): Flow<PagingData<ArticleBean>> {
        return SystemRepository.getSystemArticle(cid)
    }
}
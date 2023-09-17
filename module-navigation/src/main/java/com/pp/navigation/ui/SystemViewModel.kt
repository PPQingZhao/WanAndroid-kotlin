package com.pp.navigation.ui

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.pp.base.ThemeViewModel
import com.pp.common.http.wanandroid.bean.ArticleListBean
import com.pp.navigation.repository.SystemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SystemViewModel(app: Application) : ThemeViewModel(app) {

    private val _systemList = MutableStateFlow<List<ArticleListBean>>(emptyList())
    val systemList = _systemList.asStateFlow()

    private val _articlesList = MutableStateFlow<List<ArticleListBean>>(emptyList())
    val articleList = _articlesList.asStateFlow()
    fun getSystemList() {
        viewModelScope.launch(Dispatchers.IO) {
            SystemRepository.getSystemList().let {
                if (null == it.data) {
                    return@let
                }
                _systemList.emit(it.data!!)
                val articleList = mutableListOf<ArticleListBean>()
                it.data!!.onEach {
                    articleList.add(it)
                    it.children?.let { it1 -> articleList.addAll(it1) }
                }
                _articlesList.emit(articleList)

            }
        }
    }

}
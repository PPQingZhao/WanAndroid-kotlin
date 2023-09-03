package com.pp.navigation.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pp.base.ThemeViewModel
import com.pp.common.http.wanandroid.bean.ArticleListBean
import com.pp.ui.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TabSystemViewModel(app: Application) : ThemeViewModel(app) {


    val title = MutableLiveData<String>()
    val count = MutableLiveData<String>()
    private val _articleList = MutableStateFlow<List<ArticleListBean>>(emptyList())
    val articleList = _articleList.asStateFlow()

    fun setArticleListBean(articleListBean: ArticleListBean?) {
        viewModelScope.launch {
            if (articleListBean?.children == null || articleListBean.children?.size == 0) {
                title.value = ""
                count.value = ""
            } else {
                title.value = articleListBean.name
                count.value = getApplication<Application>().getString(R.string.chapter_count)
                    .format(articleListBean.children?.size ?: 0)
                _articleList.emit(articleListBean.children!!)
            }
        }
    }


}
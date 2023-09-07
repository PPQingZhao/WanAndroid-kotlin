package com.pp.home.ui

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.pp.base.ThemeViewModel
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.HotKey
import com.pp.common.paging.onePager
import com.pp.home.repository.SearchRepository
import com.pp.ui.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SearchViewModel(app: Application) : ThemeViewModel(app) {

    private val _hotKey = MutableSharedFlow<List<HotKey>>()
    val hotKey = _hotKey.asSharedFlow()
    private fun getSearchHot() {
        viewModelScope.launch(Dispatchers.IO) {
            SearchRepository.getHotKey().data.let {

                if (it?.isNotEmpty() == true) {
                    val dataList = mutableListOf<HotKey>()
                    HotKey(name = getApplication<Application>().resources.getString(R.string.hot_key)).also {
                        dataList.add(it)
                    }
                    dataList.addAll(it)
                    _hotKey.emit(dataList)
                } else {
                    _hotKey.emit(emptyList())
                }
            }
        }
    }

    fun getSearchHot2(): Flow<PagingData<HotKey>> {
        return onePager(getPageData = { SearchRepository.getHotKey().data },
            getPageValue = {
                if (it?.isNotEmpty() == true) {
                    mutableListOf<HotKey>().apply {
                        HotKey(name = getApplication<Application>().resources.getString(R.string.hot_key)).also {
                            add(it)
                        }
                        addAll(it)
                    }
                } else {
                    emptyList()
                }
            }).flow
    }

    fun searchPageData(key: String?): Flow<PagingData<ArticleBean>> {
        return SearchRepository.searchPageData(key)
    }

}
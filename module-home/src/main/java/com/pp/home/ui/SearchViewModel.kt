package com.pp.home.ui

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pp.base.ThemeViewModel
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.HotKey
import com.pp.common.paging.onePager
import com.pp.home.repository.SearchRepository
import com.pp.ui.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SearchViewModel(app: Application) : ThemeViewModel(app) {

    // 热门搜索
    fun getSearchHot(): Flow<PagingData<HotKey>> {
        return onePager(getPageData = { SearchRepository.getHotKey().data },
            getPageValue = {
                mutableListOf<HotKey>().apply {
                    if (it?.isNotEmpty() == true) {
                        HotKey(name = getApplication<Application>().resources.getString(R.string.hot_key)).also {
                            add(it)
                        }
                        addAll(it)
                    }
                }
            }).flow.cachedIn(viewModelScope)
    }

    fun searchPageData(key: String?): Flow<PagingData<ArticleBean>> {
        return SearchRepository.searchPageData(key).cachedIn(viewModelScope)
    }

    fun getSearchHotkeyHistory(): Flow<List<HotKey>> {
        return SearchRepository.getSearchHotkeyHistory().map {
            mutableListOf<HotKey>().apply {
                add(HotKey(name = getApplication<Application>().resources.getString(R.string.search_history)))
                addAll(it)
            }
        }
    }

    fun saveSearchHotKeyHistory(history: String) {
        viewModelScope.launch(Dispatchers.IO) {
            SearchRepository.saveSearchHotKeyHistory(history)
        }
    }

}
package com.pp.home.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
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

    val isDeleteModel = MutableLiveData(false)

    // 热门搜索
    fun getSearchHot(): Flow<PagingData<HotKey>> {
        return onePager(getPageData = { SearchRepository.getHotKey().data },
            getPageValue = {
                it ?: emptyList()
            }).flow.map {
            it.insertSeparators { before, after ->
                if (before == null && after != null) {
                    HotKey(
                        name = getApplication<Application>().resources.getString(R.string.hot_key)
                    )
                } else {
                    null
                }
            }
        }.cachedIn(viewModelScope)
    }

    fun searchPageData(key: String?): Flow<PagingData<ArticleBean>> {
        return SearchRepository.searchPageData(key).cachedIn(viewModelScope)
    }

    /**
     * 获取搜索记录
     */
    fun getSearchHotkeyHistory(): Flow<List<HotKey>> {
        return SearchRepository.getSearchHotkeyHistory().map {
            mutableListOf<HotKey>().apply {
                if (it.isEmpty()) {
                    return@apply
                }
                add(HotKey(name = getApplication<Application>().resources.getString(R.string.search_history)))
                addAll(it)
            }
        }
    }

    /**
     * 保存搜索记录
     */
    fun saveSearchHotKeyHistory(history: String) {
        viewModelScope.launch(Dispatchers.IO) {
            SearchRepository.saveSearchHotKeyHistory(history)
        }
    }

    fun clearSearchHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            SearchRepository.clearSearchHistory()
        }
    }

    fun removeSearchHistory(history: String) {
        viewModelScope.launch(Dispatchers.IO) {
            SearchRepository.removeSearchHistory(history)
        }
    }

}
package com.pp.home.ui

import android.app.Application
import androidx.paging.PagingData
import com.pp.base.ThemeViewModel
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.HotKey
import com.pp.common.paging.onePager
import com.pp.home.repository.SearchRepository
import com.pp.ui.R
import kotlinx.coroutines.flow.Flow

class SearchViewModel(app: Application) : ThemeViewModel(app) {

    fun getSearchHot(): Flow<PagingData<HotKey>> {
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
package com.pp.project.ui

import android.app.Application
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.pp.base.ThemeViewModel
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.project.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class CidProjectViewModel(app: Application) : ThemeViewModel(app) {

    private var mCid = 0

    fun setCid(cid: Int) {
        mCid = cid
    }

    suspend fun getPageData(): StateFlow<PagingData<ArticleBean>> {
        return ProjectRepository.getPageData(mCid).stateIn(viewModelScope)
    }
}
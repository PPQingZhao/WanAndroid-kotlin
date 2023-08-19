package com.pp.home.ui

import android.app.Application
import androidx.paging.PagingData
import com.pp.base.ThemeViewModel
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.home.repository.HomeRepository
import com.pp.home.repository.SquareRepository
import kotlinx.coroutines.flow.Flow

abstract class ArticleListViewModel(app: Application) : ThemeViewModel(app) {
    abstract fun getPageData(): Flow<PagingData<ArticleBean>>
}
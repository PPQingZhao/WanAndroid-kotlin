package com.pp.navigation.ui

import android.app.Application
import androidx.paging.PagingData
import com.pp.base.ThemeViewModel
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.navigation.repository.SystemRepository
import kotlinx.coroutines.flow.Flow

class ArticleListViewModel(app: Application) : ThemeViewModel(app) {

    private var mCid = 0

    fun setCid(cid: Int) {
        mCid = cid
    }


    fun getSystemArticle(): Flow<PagingData<ArticleBean>> {
        return SystemRepository.getSystemArticle(mCid)
    }

}
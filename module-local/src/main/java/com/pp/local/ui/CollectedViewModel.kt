package com.pp.local.ui

import android.app.Application
import android.view.View
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pp.base.ThemeViewModel
import com.pp.common.http.wanandroid.api.WanAndroidService
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.PageBean
import com.pp.common.http.wanandroid.bean.ResponseBean
import com.pp.common.paging.WanPagingSource
import com.pp.common.router.MultiRouterFragmentViewModel
import com.pp.common.util.ViewTreeMultiRouterFragmentViewModel
import com.pp.router_service.RouterPath
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CollectedViewModel(app: Application) : ThemeViewModel(app) {

    fun onBack(view: View) {
        ViewTreeMultiRouterFragmentViewModel.get<MultiRouterFragmentViewModel>(
            view
        )?.run {
            popBackStack(RouterPath.Local.fragment_collected)
        }
    }

    fun getCollectedPageData(): Flow<PagingData<ArticleBean>> {
        return Pager(
            initialKey = 0,
            config = PagingConfig(15),
            pagingSourceFactory = { CollectedPageSources() }).flow.cachedIn(viewModelScope)
    }

    suspend fun unCollected(id: Int, originId: Long): ResponseBean<Any> {
        return WanAndroidService.wanApi.unCollectedArticle2(id, originId)
    }

    private class CollectedPageSources() : WanPagingSource() {

        override suspend fun getPageData(page: Int): PageBean? {
            return WanAndroidService.wanApi.getCollectedArticles(page).data
        }

        override fun createNextKey(response: PageBean?): Int? {
            return super.createNextKey(response)?.plus(1)
        }

    }

}
package com.pp.home.ui

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.pp.base.ThemeViewModel
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.home.BannerBean
import com.pp.common.paging.itemArticlePagingAdapter
import com.pp.common.repository.UserRepository
import com.pp.common.repository.getPreferenceUserWhenResume
import com.pp.home.repository.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RealHomeViewModel(app: Application) : ThemeViewModel(app) {

    private val _bannerFlow = MutableStateFlow<List<BannerBean>>(emptyList())
    val bannerFlow = _bannerFlow.asStateFlow()

    val mArticleAdapter by lazy {
        itemArticlePagingAdapter(mTheme, viewModelScope)
    }

    private fun getBanner() {
        viewModelScope.launch(Dispatchers.IO) {
            _bannerFlow.value = HomeRepository.getBanner().data ?: emptyList()
        }
    }

    private fun getPageData(): Flow<PagingData<ArticleBean>> {
        return HomeRepository.getPageData()
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            getBanner()
            mArticleAdapter.refresh()
        }
    }

    override fun onCreate(owner: LifecycleOwner) {
        // 登录用户发生变化时,resume状态刷新界面
        UserRepository.getPreferenceUserWhenResume(owner) {
            refresh()
        }
    }

    override fun onFirstResume(owner: LifecycleOwner) {
        getBanner()
        viewModelScope.launch(Dispatchers.IO) {
            getPageData().collectLatest {
                mArticleAdapter.setPagingData(viewModelScope, it)
            }
        }
    }
}
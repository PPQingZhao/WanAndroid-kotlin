package com.pp.project.ui

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.pp.base.ThemeViewModel
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.paging.articleDifferCallback
import com.pp.common.paging.collectedListener
import com.pp.common.paging.itemProjectArticleBinder
import com.pp.common.repository.UserRepository
import com.pp.common.repository.getPreferenceUserWhenResume
import com.pp.project.repository.ProjectRepository
import com.pp.ui.R
import com.pp.ui.adapter.BindingPagingDataAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CidProjectViewModel(app: Application) : ThemeViewModel(app) {

    private var mCid = 0

    fun setCid(cid: Int) {
        mCid = cid
    }

    suspend fun getPageData(): StateFlow<PagingData<ArticleBean>> {
        return ProjectRepository.getPageData(mCid).stateIn(viewModelScope)
    }

    fun refresh() {
        mAdapter.refresh()
    }

    val mAdapter = BindingPagingDataAdapter<ArticleBean>(
        { R.layout.item_projectarticle },
        diffCallback = articleDifferCallback
    ).apply {
        itemProjectArticleBinder(mTheme, viewModelScope).also {
            addItemViewModelBinder(it)
        }

        collectedListener(viewModelScope)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        UserRepository.getPreferenceUserWhenResume(owner) {
            refresh()
        }
    }

    override fun onFirstResume(owner: LifecycleOwner) {
        viewModelScope.launch(Dispatchers.IO) {
            getPageData().collectLatest {
                mAdapter.setPagingData(viewModelScope, it)
            }
        }
    }
}
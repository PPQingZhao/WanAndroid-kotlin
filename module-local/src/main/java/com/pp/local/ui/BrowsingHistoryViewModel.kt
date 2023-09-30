package com.pp.local.ui

import android.app.Application
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.pp.base.ThemeViewModel
import com.pp.common.paging.browsingDifferCallback
import com.pp.common.paging.itemBrowsingHistoryBinder
import com.pp.common.repository.BrowsingHistoryRepository
import com.pp.common.repository.UserRepository
import com.pp.common.router.MultiRouterFragmentViewModel
import com.pp.database.browsing.BrowsingHistory
import com.pp.router_service.RouterPath
import com.pp.ui.R
import com.pp.ui.adapter.BindingPagingDataAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BrowsingHistoryViewModel(app: Application) : ThemeViewModel(app) {

    fun onBack(view: View) {
        MultiRouterFragmentViewModel.popBackStack(view, RouterPath.Local.fragment_browsing_history)
    }

    val mAdapter by lazy {

        BindingPagingDataAdapter<BrowsingHistory>(
            getItemLayoutRes = { R.layout.item_history },
            diffCallback = browsingDifferCallback
        ).apply {
            val itemBinder = itemBrowsingHistoryBinder(mTheme)
            addItemViewModelBinder(itemBinder)
        }
    }

    override fun onFirstResume(owner: LifecycleOwner) {
        getPagingData()
    }

    private fun getPagingData() {
        viewModelScope.launch(Dispatchers.IO) {
            BrowsingHistoryRepository.getPageData(UserRepository.getPreferenceUser()?.userId)
                .cachedIn(viewModelScope)
                .collectLatest {
                    mAdapter.setPagingData(viewModelScope, it)
                }
        }
    }
}
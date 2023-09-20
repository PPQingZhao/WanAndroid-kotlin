package com.pp.user.ui

import android.app.Application
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pp.base.ThemeViewModel
import com.pp.common.http.wanandroid.bean.CoinInfoBean
import com.pp.common.paging.coinRankDifferCallback
import com.pp.common.paging.itemCoinRankBinder
import com.pp.common.repository.CoinRepository
import com.pp.common.router.MultiRouterFragmentViewModel
import com.pp.common.util.ViewTreeMultiRouterFragmentViewModel
import com.pp.router_service.RouterPath
import com.pp.ui.R
import com.pp.ui.adapter.BindingPagingDataAdapter
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CoinRankViewModel(app: Application) : ThemeViewModel(app) {

    val mAdapter by lazy {
        BindingPagingDataAdapter<CoinInfoBean>(
            diffCallback = coinRankDifferCallback,
            getItemLayoutRes = {
                R.layout.item_coin_rank
            }).apply {
            addItemViewModelBinder(
                itemCoinRankBinder(mTheme)
            )
        }
    }

    override fun onFirstResume(owner: LifecycleOwner) {
        viewModelScope.launch {
            async {
                CoinRepository.getCoinRank().collectLatest {
                    mAdapter.setPagingData(this@launch, it)
                }
            }
        }
    }

    /**
     * 返回按钮点击事件
     */
    fun onBack(view: View) {
        ViewTreeMultiRouterFragmentViewModel.get<MultiRouterFragmentViewModel>(
            view
        )?.run {
            popBackStack(RouterPath.User.fragment_coin_range)
        }
    }

}
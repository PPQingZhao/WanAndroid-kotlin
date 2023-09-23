package com.pp.user.ui

import android.app.Application
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.pp.base.ThemeViewModel
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.CoinReasonBean
import com.pp.common.paging.articleDifferCallback
import com.pp.common.paging.coinReasonDifferCallback
import com.pp.common.paging.itemArticleCollectedBinder
import com.pp.common.paging.itemCoinReasonBinder
import com.pp.common.repository.CoinRepository
import com.pp.common.repository.CollectedRepository
import com.pp.common.router.MultiRouterFragmentViewModel
import com.pp.common.util.ViewTreeMultiRouterFragmentViewModel
import com.pp.router_service.RouterPath
import com.pp.ui.R
import com.pp.ui.adapter.BindingPagingDataAdapter
import com.pp.ui.viewModel.ItemDataViewModel
import com.pp.ui.viewModel.OnItemListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CoinViewModel(app: Application) : ThemeViewModel(app) {

    val coin = MutableLiveData<String>("000")

    val mAdapter by lazy {
        BindingPagingDataAdapter<CoinReasonBean>(
            diffCallback = coinReasonDifferCallback,
            getItemLayoutRes = {
                R.layout.item_coin_reason
            }).apply {
            addItemViewModelBinder(
                itemCoinReasonBinder(mTheme)
            )
        }
    }


    fun getCoinInfo() {
        viewModelScope.launch {
            CoinRepository.getCoinInfo().data.let {
                coin.value = (it?.coinCount ?: 0).toString()
            }
        }
    }

    private fun getCoinList() {
        viewModelScope.launch(Dispatchers.IO) {
            CoinRepository.getCoinList().collectLatest {
                mAdapter.setPagingData(viewModelScope, it)
            }
        }
    }

    override fun onFirstResume(owner: LifecycleOwner) {
        getCoinInfo()
        getCoinList()
    }

    /**
     * 返回按钮点击事件
     */
    fun onBack(view: View) {
        ViewTreeMultiRouterFragmentViewModel.get<MultiRouterFragmentViewModel>(
            view
        )?.run {
            popBackStack(RouterPath.User.fragment_coin)
        }
    }

    /**
     * 积分排行榜
     */
    fun onCoinRange(view: View) {
        ViewTreeMultiRouterFragmentViewModel.get<MultiRouterFragmentViewModel>(
            view
        )?.run {
            showFragment(RouterPath.User.fragment_coin_range, RouterPath.User.fragment_coin_range)
        }
    }


}
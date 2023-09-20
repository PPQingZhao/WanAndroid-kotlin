package com.pp.user.ui

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
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
import com.pp.module_user.repositoy.UserRepository
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

    val avatar = ObservableField<String>()
    val nickname = ObservableField<String>()
    val coinCount = ObservableField<String>()
    val rank = ObservableField<String>()
    override fun onFirstResume(owner: LifecycleOwner) {
        viewModelScope.launch {
            async {
                CoinRepository.getCoinRank().collectLatest {
                    mAdapter.setPagingData(this@launch, it)
                }
            }

            async {
                UserRepository.getUserInfo().apply {
                    avatar.set(data?.userInfo?.icon)
                    nickname.set(data?.userInfo?.nickname)
                    coinCount.set(data?.coinInfo?.coinCount.toString())
                    rank.set(data?.coinInfo?.rank)
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

    fun coinCount(coin: String?): String {
        return getApplication<Application>().getString(R.string.coin_value).format(coin ?: "0")
    }

    fun rank(rank: String?): String {
        return getApplication<Application>().getString(R.string.rank_value).format(rank ?: "0")
    }

}
package com.pp.common.model

import com.pp.common.http.wanandroid.bean.CoinInfoBean
import com.pp.common.http.wanandroid.bean.CoinReasonBean
import com.pp.theme.AppDynamicTheme
import com.pp.ui.viewModel.ItemCoinRankViewModel
import com.pp.ui.viewModel.ItemCoinReasonViewModel

class ItemCoinRankItemViewModel(
    bean: CoinInfoBean?,
    theme: AppDynamicTheme,
) : ItemCoinRankViewModel<CoinInfoBean>(theme) {
    init {
        data = bean
    }

    override fun onUpdateData(data: CoinInfoBean?) {
        data.let { field ->
            rank.set(field?.rank)
            username.set(field?.username)
            coinCount.set("${field?.coinCount ?: 0}")
        }
    }

}
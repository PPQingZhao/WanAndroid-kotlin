package com.pp.common.model

import com.pp.common.http.wanandroid.bean.CoinReasonBean
import com.pp.theme.AppDynamicTheme
import com.pp.ui.viewModel.ItemCoinReasonViewModel

class ItemCoinReasonItemViewModel(
    bean: CoinReasonBean?,
    theme: AppDynamicTheme,
) : ItemCoinReasonViewModel<CoinReasonBean>(theme) {
    init {
        data = bean
    }

    override fun onUpdateData(data: CoinReasonBean?) {
        data.let { field ->
            coinReason.set(field?.reason)
            desc.set(field?.desc)
            coin.set("+ ${field?.coinCount ?: 0}")
        }
    }

}
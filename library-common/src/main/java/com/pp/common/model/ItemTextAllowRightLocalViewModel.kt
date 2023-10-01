package com.pp.common.model

import com.pp.theme.AppDynamicTheme
import com.pp.ui.viewModel.ItemLocalBean
import com.pp.ui.viewModel.ItemTextAllowRightViewModel

class ItemTextAllowRightLocalViewModel(localBean: ItemLocalBean?, theme: AppDynamicTheme) :
    ItemTextAllowRightViewModel<ItemLocalBean>(theme) {

    init {
        data = localBean
    }

    override fun onUpdateData(data: ItemLocalBean?) {
        content.set(data?.leftText ?: 0)
    }
}
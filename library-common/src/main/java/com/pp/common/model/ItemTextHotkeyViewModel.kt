package com.pp.common.model

import com.pp.common.http.wanandroid.bean.HotKey
import com.pp.theme.AppDynamicTheme
import com.pp.ui.viewModel.ItemTextViewModel

class ItemTextHotkeyViewModel(hotKey: () -> HotKey?, theme: AppDynamicTheme) :
    ItemTextViewModel<HotKey>(theme) {

    init {
        data = hotKey
    }

    override fun onUpdateData(data: HotKey?) {
        text.set(data?.name ?: "")
    }
}
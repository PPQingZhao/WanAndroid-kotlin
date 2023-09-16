package com.pp.common.model

import com.pp.common.http.wanandroid.bean.HotKey
import com.pp.theme.AppDynamicTheme
import com.pp.ui.viewModel.ItemDeleteViewModel

class ItemTextDeleteHotkeyViewModel(hotKey: () -> HotKey?, theme: AppDynamicTheme) :
    ItemDeleteViewModel<HotKey>(theme) {

    init {
        data = hotKey
    }

    override fun onUpdateData(data: HotKey?) {
        text.set(data?.name ?: "")
    }

}
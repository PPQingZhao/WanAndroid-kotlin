package com.pp.common.model

import com.pp.common.http.wanandroid.bean.HotKeyBean
import com.pp.theme.AppDynamicTheme
import com.pp.ui.viewModel.ItemDeleteViewModel

class ItemTextDeleteHotkeyViewModel(hotKey: HotKeyBean?, theme: AppDynamicTheme) :
    ItemDeleteViewModel<HotKeyBean>(theme) {

    init {
        data = hotKey
    }

    override fun onUpdateData(data: HotKeyBean?) {
        text.set(data?.name ?: "")
    }

}
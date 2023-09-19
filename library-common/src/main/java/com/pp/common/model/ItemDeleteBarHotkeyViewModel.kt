package com.pp.common.model

import android.view.View
import com.pp.common.http.wanandroid.bean.HotKey
import com.pp.theme.AppDynamicTheme
import com.pp.ui.viewModel.ItemDeleteViewModel

class ItemDeleteBarHotkeyViewModel(hotKey:HotKey?, theme: AppDynamicTheme) :
    ItemDeleteViewModel<HotKey>(theme) {

    init {
        data = hotKey
    }

    override fun onUpdateData(data: HotKey?) {
        text.set(data?.name ?: "")
    }

    override fun onItemViewModelClick(view: View): Boolean {
        val result = super.onItemViewModelClick(view)
        when (view.id) {
            com.pp.ui.R.id.tv_delete_all,
            com.pp.ui.R.id.tv_finish,
            -> {
                isDeleteModel.value = false
            }
            com.pp.ui.R.id.iv_delete_model -> {
                isDeleteModel.value = true
            }
        }
        return result
    }
}
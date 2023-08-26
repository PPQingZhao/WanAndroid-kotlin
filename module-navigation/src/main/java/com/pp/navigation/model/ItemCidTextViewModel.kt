package com.pp.navigation.model

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pp.common.http.wanandroid.bean.ArticleCidBean
import com.pp.theme.AppDynamicTheme
import com.pp.ui.viewModel.ItemTextViewModel

class ItemCidTextViewModel(cidBean: ArticleCidBean?, theme: AppDynamicTheme) :
    ItemTextViewModel<ArticleCidBean>(theme) {

    companion object {
        fun reset() {
            _SeletedItemModel.value = null
        }

        private val _SeletedItemModel = MutableLiveData<ItemCidTextViewModel>()
        val selectedItemModel: LiveData<ItemCidTextViewModel> = _SeletedItemModel
    }

    init {
        updateData(cidBean)
    }

    override fun onUpdateData(data: ArticleCidBean?) {
        text.set(data?.name)
    }

    override fun onItemClick(view: View) {
        _SeletedItemModel.value?.isSelected?.set(false)
        super.onItemClick(view)
        _SeletedItemModel.value = this

    }

}
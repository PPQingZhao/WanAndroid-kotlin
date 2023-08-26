package com.pp.navigation.model

import android.view.View
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.pp.common.http.wanandroid.bean.ArticleListBean
import com.pp.theme.AppDynamicTheme
import com.pp.ui.viewModel.ItemTextViewModel

class ItemArticleListTextViewModel(cidBean: ArticleListBean?, theme: AppDynamicTheme) :
    ItemTextViewModel<ArticleListBean>(theme) {

    companion object {
        fun selectedItem(item: ItemArticleListTextViewModel?) {
            if (_SeletedItemModel.value == item) {
                return
            }
            _SeletedItemModel.value?.isSelected?.set(false)
            _SeletedItemModel.value = item?.apply { isSelected.set(true) }
        }

        private val _SeletedItemModel = MutableLiveData<ItemArticleListTextViewModel?>()

        fun getSelectedItem():ItemArticleListTextViewModel?{
            return _SeletedItemModel.value
        }
        fun observerSelectedItem(
            owner: LifecycleOwner,
            observer: Observer<ItemArticleListTextViewModel?>,
        ) {
            owner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    selectedItem(null)
                }
            })
            selectedItem(null)
            _SeletedItemModel.observe(owner, observer)
        }


    }

    init {
        data = cidBean
    }

    override fun onUpdateData(data: ArticleListBean?) {
        text.set(data?.name)
    }

    override fun onItemClick(view: View) {
        selectedItem(this)

    }

}
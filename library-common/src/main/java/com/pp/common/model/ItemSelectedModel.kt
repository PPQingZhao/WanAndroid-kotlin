package com.pp.common.model

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.pp.ui.viewModel.ItemDataViewModel

class ItemSelectedModel<Data : Any, Item : ItemDataViewModel<Data>> {


    fun selectedItem(item: Item?) {
        if (_SeletedItemModel.value == item) {
            return
        }
        _SeletedItemModel.value?.isSelected?.set(false)
        _SeletedItemModel.value = item?.apply { isSelected.set(true) }
    }

    private val _SeletedItemModel = MutableLiveData<Item?>()

    fun getSelectedItem(): Item? {
        return _SeletedItemModel.value
    }

    fun observerSelectedItem(
        owner: LifecycleOwner,
        observer: Observer<Item?>,
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
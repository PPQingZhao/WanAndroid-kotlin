package com.pp.common.model

import androidx.databinding.Observable
import androidx.databinding.Observable.OnPropertyChangedCallback
import androidx.datastore.preferences.core.edit
import com.pp.common.app.App
import com.pp.common.constant.preferences_key_floating_theme_setting
import com.pp.common.datastore.userDataStore
import com.pp.theme.AppDynamicTheme
import com.pp.ui.viewModel.ItemCheckedViewModel
import com.pp.ui.viewModel.ItemLocalBean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class ItemCheckedLocalViewModel(
    scope: CoroutineScope,
    localBean: ItemLocalBean?,
    theme: AppDynamicTheme,
) :
    ItemCheckedViewModel<ItemLocalBean>(theme) {

    init {
        data = localBean

        scope.launch {
            App.getInstance().userDataStore.data.firstOrNull()?.get(
                preferences_key_floating_theme_setting
            ).run {
                checked.set(this ?: false)
            }
        }


        checked.addOnPropertyChangedCallback(object : OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                scope.launch {
                    App.getInstance().userDataStore.edit {
                        it[preferences_key_floating_theme_setting] = checked.get()
                    }
                }
            }
        })
    }

    override fun onUpdateData(data: ItemLocalBean?) {
        content.set(data?.leftText ?: 0)
    }

}
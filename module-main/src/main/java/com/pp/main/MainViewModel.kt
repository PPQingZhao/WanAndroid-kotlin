package com.pp.main

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.*
import com.pp.base.ThemeViewModel
import com.pp.common.constant.preferences_key_floating_theme_setting
import com.pp.common.datastore.userDataStore
import com.pp.network.utils.NetworkType
import com.pp.network.utils.registerNetStatesChangedListener
import com.pp.network.utils.unRegisterNetStatesChangedListener
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : ThemeViewModel(app) {

    private val netStateChangedObserver =
        Observer<NetworkType> {
            _showNetStateTip.value = it == NetworkType.NETWORK_NO
        }

    private val _showNetStateTip = MutableLiveData<Boolean>()
    val showNetStateTip: LiveData<Boolean> = _showNetStateTip
    val floatingThemeSettingVisibility = ObservableBoolean()

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        registerNetStatesChangedListener(owner, netStateChangedObserver)

        viewModelScope.launch {
            getApplication<Application>().userDataStore.data.collectLatest {
                floatingThemeSettingVisibility.set(it[preferences_key_floating_theme_setting] ?: false)
            }
        }
    }

    override fun onCleared() {
        unRegisterNetStatesChangedListener(netStateChangedObserver)
    }
}
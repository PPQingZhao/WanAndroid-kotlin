package com.pp.main

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.pp.base.ThemeViewModel
import com.pp.network.utils.NetworkType
import com.pp.network.utils.registerNetStatesChangedListener
import com.pp.network.utils.unRegisterNetStatesChangedListener

class MainViewModel(app: Application) : ThemeViewModel(app) {

    private val netStateChangedObserver =
        Observer<NetworkType> {
            _showNetStateTip.value = it == NetworkType.NETWORK_NO
        }

    private val _showNetStateTip = MutableLiveData<Boolean>()
    val showNetStateTip: LiveData<Boolean> = _showNetStateTip

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        registerNetStatesChangedListener(owner, netStateChangedObserver)
    }

    override fun onCleared() {
        unRegisterNetStatesChangedListener(netStateChangedObserver)
    }
}
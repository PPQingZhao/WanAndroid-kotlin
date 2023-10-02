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

class MainFragmentViewModel(app: Application) : ThemeViewModel(app) {

}
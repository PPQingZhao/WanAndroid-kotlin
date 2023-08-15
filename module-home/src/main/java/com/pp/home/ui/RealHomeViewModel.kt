package com.pp.home.ui

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.pp.base.ThemeViewModel
import com.pp.common.http.wanandroid.bean.home.BannerBean
import com.pp.home.repository.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RealHomeViewModel(app: Application) : ThemeViewModel(app) {

    private val _bannerFlow = MutableStateFlow<List<BannerBean>>(emptyList())
    val bannerFlow = _bannerFlow.asStateFlow()

    fun getBanner2() {
        viewModelScope.launch(Dispatchers.IO) {
            _bannerFlow.value = HomeRepository.getBanner().data ?: mutableListOf()
        }
    }
}
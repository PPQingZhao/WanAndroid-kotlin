package com.pp.home.ui

import android.app.Application
import com.pp.base.ThemeViewModel
import com.pp.common.http.wanandroid.bean.ResponseBean
import com.pp.common.http.wanandroid.bean.home.BannerBean
import com.pp.home.repository.HomeRepository

class RealHomeViewModel(app: Application) : ThemeViewModel(app) {

    suspend fun getBanner(): ResponseBean<List<BannerBean>> {
        return HomeRepository.getBanner()
    }
}
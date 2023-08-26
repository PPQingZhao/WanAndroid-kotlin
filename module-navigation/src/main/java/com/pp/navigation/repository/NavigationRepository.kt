package com.pp.navigation.repository

import com.pp.common.http.wanandroid.api.WanAndroidService
import com.pp.common.http.wanandroid.bean.ArticleCidBean
import com.pp.common.http.wanandroid.bean.ResponseBean

object NavigationRepository {

    suspend fun getNavigation(): ResponseBean<List<ArticleCidBean>> {
        return WanAndroidService.navigationApi.getNavigation()
    }
}
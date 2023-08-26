package com.pp.navigation.repository

import com.pp.common.http.wanandroid.api.WanAndroidService
import com.pp.common.http.wanandroid.bean.ArticleListBean
import com.pp.common.http.wanandroid.bean.ResponseBean

object NavigationRepository {

    suspend fun getNavigation(): ResponseBean<List<ArticleListBean>> {
        return WanAndroidService.navigationApi.getNavigation()
    }

}
package com.pp.navigation.repository

import com.pp.common.http.wanandroid.api.WanAndroidService
import com.pp.common.http.wanandroid.bean.ArticleListBean
import com.pp.common.http.wanandroid.bean.ResponseBean
import com.pp.common.http.wanandroid.bean.runCatchingResponse

object NavigationRepository {

    suspend fun getNavigation(): ResponseBean<List<ArticleListBean>> {
        return runCatchingResponse {
            WanAndroidService.navigationApi.getNavigation()
        }
    }

}
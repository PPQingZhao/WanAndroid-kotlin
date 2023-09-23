package com.pp.project.ui

import android.app.Application
import com.pp.base.ThemeViewModel
import com.pp.common.http.wanandroid.bean.ArticleListBean
import com.pp.common.http.wanandroid.bean.ResponseBean
import com.pp.project.repository.ProjectRepository

class ProjectViewModel(app: Application) : ThemeViewModel(app) {

    suspend fun getProjectCid(): ResponseBean<List<ArticleListBean>> {
        return ProjectRepository.getProjectCid()
    }
}
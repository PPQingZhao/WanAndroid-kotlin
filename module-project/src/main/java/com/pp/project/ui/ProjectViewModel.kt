package com.pp.project.ui

import android.app.Application
import com.pp.base.ThemeViewModel
import com.pp.common.http.wanandroid.bean.ArticleCidBean
import com.pp.project.repository.ProjectRepository

class ProjectViewModel(app: Application) : ThemeViewModel(app) {

    suspend fun getProjectCid(): List<ArticleCidBean> {
        return ProjectRepository.getProjectCid()
    }
}
package com.pp.project.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.pp.base.ThemeFragment
import com.pp.project.databinding.FragmentProjectBinding
import com.pp.router_service.RouterPath

@Route(path = RouterPath.Project.fragment_project)
class ProjectFragment : ThemeFragment<FragmentProjectBinding, ProjectViewModel>() {
    override val mBinding: FragmentProjectBinding by lazy {
        FragmentProjectBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<ProjectViewModel> {
        return ProjectViewModel::class.java
    }
}
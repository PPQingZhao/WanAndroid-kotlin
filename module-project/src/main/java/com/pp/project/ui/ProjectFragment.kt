package com.pp.project.ui

import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.pp.base.ThemeFragment
import com.pp.base.helper.TabPagerFragmentHelper
import com.pp.common.http.wanandroid.bean.ArticleCidBean
import com.pp.project.databinding.FragmentProjectBinding
import com.pp.router_service.RouterPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Route(path = RouterPath.Project.fragment_project)
class ProjectFragment : ThemeFragment<FragmentProjectBinding, ProjectViewModel>() {
    override val mBinding: FragmentProjectBinding by lazy {
        FragmentProjectBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<ProjectViewModel> {
        return ProjectViewModel::class.java
    }

    override fun onFirstResume() {

        lifecycleScope.launch(Dispatchers.IO) {

            val projectCid = mViewModel.getProjectCid()
            val pagers = getPagers(projectCid)
            withContext(Dispatchers.Main) {

                mBinding.viewpager2.offscreenPageLimit = 1
                TabPagerFragmentHelper(childFragmentManager, viewLifecycleOwner.lifecycle)
                    .addPagers(pagers)
                    .attach(mBinding.tablayout, mBinding.viewpager2)
            }
        }
    }

    private fun getPagers(cidList: List<ArticleCidBean>): List<TabPagerFragmentHelper.TabPager> {
        val pagers = mutableListOf<TabPagerFragmentHelper.TabPager>()
        cidList.onEach {
            TabPagerFragmentHelper.TabPager(
                fragment = { CidProjectFragment.newInstance(it.id) },
                title = it.name
            ).also {
                pagers.add(it)
            }
        }
        return pagers
    }

}
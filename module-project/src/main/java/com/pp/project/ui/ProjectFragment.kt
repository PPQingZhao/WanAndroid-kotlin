package com.pp.project.ui

import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.pp.base.ThemeFragment
import com.pp.base.helper.TabPagerFragmentHelper
import com.pp.common.http.wanandroid.bean.ArticleListBean
import com.pp.project.databinding.FragmentProjectBinding
import com.pp.router_service.RouterPath
import com.pp.ui.utils.StateView
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


    private val mStateView by lazy {

        StateView.DefaultBuilder(mBinding.contenParent, mViewModel.mTheme, viewLifecycleOwner)
            .setOnRetry {
                viewLifecycleOwner.lifecycleScope.launch {
                    getProjects()
                }
            }
            .build()
    }

    override fun onFirstResume() {

        mStateView.showLoading()
        lifecycleScope.launch {
            getProjects()
        }
    }

    private suspend fun getProjects() {

        withContext(Dispatchers.Main) {

            kotlin.runCatching {
                val projectCid =
                    withContext(Dispatchers.IO) {
                        mViewModel.getProjectCid()
                    }

                val pagers = getPagers(projectCid)
                if (projectCid.isEmpty()) {
                    mStateView.showEmpty()
                } else {
                    mStateView.showContent()
                    mBinding.viewpager2.offscreenPageLimit = 1
                    TabPagerFragmentHelper(childFragmentManager, viewLifecycleOwner.lifecycle)
                        .addPagers(pagers)
                        .attach(mBinding.tablayout, mBinding.viewpager2)
                }
            }.getOrElse {
                withContext(Dispatchers.Main) {
                    mStateView.showError(it)
                }
            }
        }
    }

    private fun getPagers(cidList: List<ArticleListBean>): List<TabPagerFragmentHelper.TabPager> {
        val pagers = mutableListOf<TabPagerFragmentHelper.TabPager>()
        cidList.onEach {
            TabPagerFragmentHelper.TabPager(
                fragment = { CidProjectFragment.newInstance(it.id) },
                title = it.name ?: ""
            ).also {
                pagers.add(it)
            }
        }
        return pagers
    }

}
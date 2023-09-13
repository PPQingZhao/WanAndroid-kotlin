package com.pp.navigation.ui

import android.os.Build
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.transition.MaterialSharedAxis
import com.pp.base.ThemeFragment
import com.pp.base.helper.TabPagerFragmentHelper
import com.pp.common.app.App
import com.pp.common.constant.ON_BACK_PRESSED
import com.pp.common.http.wanandroid.bean.ArticleListBean
import com.pp.common.router.MultiRouterFragmentViewModel
import com.pp.common.util.ViewTreeMultiRouterFragmentViewModel
import com.pp.common.util.materialSharedAxis
import com.pp.navigation.databinding.FragmentTabSystemBinding
import com.pp.router_service.RouterPath
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Route(path = RouterPath.Navigation.fragment_tab_system)
class TabSystemFragment :
    ThemeFragment<FragmentTabSystemBinding, TabSystemViewModel>() {
    override val mBinding: FragmentTabSystemBinding by lazy {
        FragmentTabSystemBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<TabSystemViewModel> {
        return TabSystemViewModel::class.java
    }

    companion object {
        const val SYSTEM_ARTICLE_LIST = "system_article_list"
        const val TARGET_POSITION = "target_position"
    }

    private var targetPosition = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = materialSharedAxis(MaterialSharedAxis.Z, false)
        enterTransition = materialSharedAxis(MaterialSharedAxis.Z, true)
        reenterTransition = materialSharedAxis(MaterialSharedAxis.Z, true)

        arguments?.apply {
            targetPosition = getInt(TARGET_POSITION)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getParcelable(
                    SYSTEM_ARTICLE_LIST, ArticleListBean::class.java
                )
            } else {
                getParcelable(SYSTEM_ARTICLE_LIST)
            }.apply {
                mViewModel.setArticleListBean(this)
            }
        }

        enableBackPressed(true)
        initView()
    }

    override fun handleOnBackPressed() {
        onBackPressed()
    }

    private fun initView() {
        mBinding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun onBackPressed() {
        ViewTreeMultiRouterFragmentViewModel.get<MultiRouterFragmentViewModel>(
            mBinding.root
        )?.run {
            popBackStack(RouterPath.Navigation.fragment_tab_system)
        }
    }

    override fun onFirstResume() {
        lifecycleScope.launch {
            mViewModel.articleList.collectLatest {
                mBinding.viewpager2.offscreenPageLimit = 1
                TabPagerFragmentHelper(childFragmentManager, viewLifecycleOwner.lifecycle)
                    .addPagers(getPagers(it))
                    .attach(mBinding.tablayout, mBinding.viewpager2)
                if (targetPosition != -1) {
                    mBinding.viewpager2.setCurrentItem(targetPosition, false)
                    targetPosition = -1
                }

            }
        }
    }

    private fun getPagers(cidList: List<ArticleListBean>): List<TabPagerFragmentHelper.TabPager> {
        val pagers = mutableListOf<TabPagerFragmentHelper.TabPager>()
        cidList.onEach {
            TabPagerFragmentHelper.TabPager(
                fragment = { ArticleListFragment.newInstance(it.id) },
                title = it.name ?: ""
            ).also {
                pagers.add(it)
            }
        }
        return pagers
    }

}
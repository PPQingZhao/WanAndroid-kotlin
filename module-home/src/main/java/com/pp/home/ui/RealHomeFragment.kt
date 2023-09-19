package com.pp.home.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.view.doOnAttach
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.pp.base.ThemeFragment
import com.pp.base.browser.WebViewFragment
import com.pp.common.browser.CommonWebViewFragment
import com.pp.common.http.wanandroid.bean.home.BannerBean
import com.pp.common.paging.itemArticlePagingAdapter
import com.pp.common.router.MultiRouterFragmentViewModel
import com.pp.common.util.ViewTreeMultiRouterFragmentViewModel
import com.pp.home.databinding.FragmentHomeChildRealhomeBinding
import com.pp.home.databinding.FragmentHomeChildRealhomeBindingImpl
import com.pp.router_service.RouterPath
import com.pp.ui.adapter.IndicatorTransitionListener
import com.pp.ui.utils.attachStateView
import com.pp.ui.utils.BannerCarousel.Adapter
import com.pp.ui.utils.StateView
import com.pp.ui.utils.loadOriginal
import com.pp.ui.utils.setPagingAdapter
import com.pp.ui.widget.BannerMotionLayoutScrollAbility
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RealHomeFragment :
    ThemeFragment<FragmentHomeChildRealhomeBinding, RealHomeViewModel>() {
    override val mBinding: FragmentHomeChildRealhomeBinding by lazy {
        FragmentHomeChildRealhomeBindingImpl.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<RealHomeViewModel> {
        return RealHomeViewModel::class.java
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBanner()
        initIndicator()
        initStateView()
    }

    private fun initStateView() {
        mBinding.refreshLayout.doOnAttach {

            StateView.DefaultBuilder(mBinding.refreshLayout, mViewModel.mTheme, viewLifecycleOwner)
                .setOnRetry {
                    mViewModel.getBanner()
                    mArticleAdapter.refresh()
                }
                .build()
                .also {
                    mArticleAdapter.attachStateView(it)
                }
        }

    }

    private fun initIndicator() {
        mBinding.carousel.setTransitionListener(IndicatorTransitionListener(mBinding.indicator))
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initBanner() {
        mBinding.banner.setChildScrollAbility(
            BannerMotionLayoutScrollAbility(
                mBinding.carousel
            )
        )

        val dataList = mutableListOf<BannerBean>()
        val bannerAdapter = object : Adapter {
            override fun count(): Int {
                return dataList.size
            }

            override fun populate(view: View?, index: Int) {

//                Log.e("TAG", " index: $index  $view")
                if (view is ImageView) {
                    view.setOnClickListener {
                        dataList[mBinding.carousel.currentIndex].let { bannerBean ->
                            val bundle = Bundle().also {
                                it.putString(WebViewFragment.WEB_VIEW_TITLE, bannerBean.title)
                                it.putString(WebViewFragment.WEB_VIEW_URL, bannerBean.url)
                                it.putString(CommonWebViewFragment.WEB_VIEW_TRANSITION_NAME, null)
                            }
                            ViewTreeMultiRouterFragmentViewModel.get<MultiRouterFragmentViewModel>(
                                mBinding.root
                            )?.run {
                                showFragment(
                                    targetFragment = RouterPath.Web.fragment_web,
                                    tag = RouterPath.Web.fragment_web,
                                    arguments = bundle
                                )
                            }
                        }
                    }

                    dataList[index].let { bannerBean ->
                        view.loadOriginal(bannerBean.imagePath)
                    }
                }
            }

            override fun onNewItem(index: Int) {
//                Log.e("TAG", "onNewItem index: $index")
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                mViewModel.bannerFlow.collectLatest {
                    dataList.clear()
                    dataList.addAll(it)

                    mBinding.carousel.setAdapter(bannerAdapter)
                    mBinding.carousel.jumpToIndex(mBinding.carousel.currentIndex)
                    updateIndicator()
                }
            }
        }
    }

    private fun updateIndicator() {
        mBinding.indicator.initIndicator(mBinding.carousel.count)
    }

    private val mArticleAdapter by lazy {
        itemArticlePagingAdapter(mViewModel.mTheme, mViewModel.viewModelScope)
    }

    private fun initPagingList() {

        mBinding.recyclerview.setPagingAdapter(
            lifecycleScope,
            mViewModel.getPageData(),
            mArticleAdapter,
            layoutManager = LinearLayoutManager(requireContext()),
            refreshLayout = mBinding.refreshLayout,
            onReFreshChildScrollUp = { mBinding.contentMotionLayout.progress > 0 }
        )
    }

    override fun onFirstResume() {
        super.onFirstResume()
        mViewModel.getBanner()
        initPagingList()
    }
}
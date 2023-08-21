package com.pp.home.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.pp.base.ThemeFragment
import com.pp.common.http.wanandroid.bean.home.BannerBean
import com.pp.common.paging.articleDifferCallback
import com.pp.home.databinding.FragmentHomeChildRealhomeBinding
import com.pp.home.model.ArticleItemArticleViewModel
import com.pp.ui.adapter.BindingPagingDataAdapter
import com.pp.ui.adapter.IndicatorTransitionListener
import com.pp.ui.databinding.ItemArticleBinding
import com.pp.ui.utils.BannerCarousel
import com.pp.ui.utils.BannerCarousel.Adapter
import com.pp.ui.utils.loadOriginal
import com.pp.ui.widget.BannerMotionLayoutScrollAbility
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RealHomeFragment :
    ThemeFragment<FragmentHomeChildRealhomeBinding, RealHomeViewModel>() {
    override val mBinding: FragmentHomeChildRealhomeBinding by lazy {
        FragmentHomeChildRealhomeBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<RealHomeViewModel> {
        return RealHomeViewModel::class.java
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBanner()
        initIndicator()
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
                    view.loadOriginal(dataList[index].imagePath)
                }
            }

            override fun onNewItem(index: Int) {
//                mBinding.indicator.setPosition(index,0f)
//                Log.e("TAG", "onNewItem index: $index")
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
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

    private fun initPagingList() {
        val adapter =
            BindingPagingDataAdapter.DefaultBindingPagingDataAdapter(
                onCreateViewDataBinding = { ItemArticleBinding.inflate(layoutInflater, it, false) },
                onCreateItemViewModel = { binding, item ->
                    val viewModel = binding.viewModel
                    if (viewModel is ArticleItemArticleViewModel) {
                        viewModel.also { it.updateArticle(item) }
                    } else {
                        ArticleItemArticleViewModel(item, mViewModel.mTheme)
                    }
                },
                diffCallback = articleDifferCallback
            )

        mBinding.pageListView.setPageAdapter(
            viewLifecycleOwner,
            lifecycleScope,
            mViewModel.getPageData(),
            adapter
        )
    }

    override fun onFirstResume() {
        super.onFirstResume()
        mViewModel.getBanner2()
        initPagingList()
    }
}
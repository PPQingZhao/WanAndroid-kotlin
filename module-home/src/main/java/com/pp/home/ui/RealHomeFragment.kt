package com.pp.home.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.pp.base.ThemeFragment
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.home.BannerBean
import com.pp.home.databinding.FragmentHomeChildRealhomeBinding
import com.pp.home.model.HomeItemArticleViewModel
import com.pp.ui.adapter.*
import com.pp.ui.databinding.ItemArticleBindingImpl
import com.pp.ui.utils.loadOriginal
import kotlinx.coroutines.Dispatchers
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
        initRecyclerView()
    }

    private val mAdapter by lazy {
        val differCallback = object : DiffUtil.ItemCallback<ArticleBean>() {
            override fun areItemsTheSame(oldItem: ArticleBean, newItem: ArticleBean): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: ArticleBean, newItem: ArticleBean): Boolean {
                return oldItem == newItem
            }
        }

        BindingPagingDataAdapter.DefaultBindingPagingDataAdapter(
            onCreateViewDataBinding = { ItemArticleBindingImpl.inflate(layoutInflater, it, false) },
            onCreateItemViewModel = { binding, item ->
                val viewModel = binding.viewModel
                if (viewModel is HomeItemArticleViewModel) {
                    viewModel.also { it.article = item }
                } else {
                    HomeItemArticleViewModel(item, mViewModel.mTheme)
                }
            },
            diffCallback = differCallback
        )
    }

    private fun initRecyclerView() {
        mBinding.recyclerview.let {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = mAdapter
        }
    }

    private fun initIndicator() {
        mBinding.motionlayout.addTransitionListener(object : TransitionAdapter() {
            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float,
            ) {
                mBinding.indicator.setPosition(mBinding.carousel.currentIndex, progress)
            }
        })
    }

    private fun initBanner() {

        val dataList = mutableListOf<BannerBean>()
        val bannerAdapter = object :
            BannerAdapter(lifecycleScope, mBinding.motionlayout, mBinding.carousel) {
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
//                        Log.e("TAG", "onNewItem index: $index")
            }
        }
        bannerAdapter.attachLifecycle(this@RealHomeFragment)
        mBinding.carousel.setAdapter(bannerAdapter)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.bannerFlow.collectLatest {
                    dataList.clear()
                    dataList.addAll(it)
                    mBinding.carousel.jumpToIndex(mBinding.carousel.currentIndex)
                    bannerAdapter.start()
                    updateIndicator()
                }
            }
        }
    }

    private fun updateIndicator() {
        mBinding.indicator.initIndicator(mBinding.carousel.count)
    }

    override fun onFirstResume() {
        super.onFirstResume()
        mViewModel.getBanner2()

        lifecycleScope.launch(Dispatchers.IO) {
            mViewModel.getPageData().collect {
                mAdapter.submitData(lifecycle, it)
            }
        }
    }
}
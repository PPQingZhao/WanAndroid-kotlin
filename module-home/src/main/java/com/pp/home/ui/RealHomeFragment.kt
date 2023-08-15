package com.pp.home.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.pp.base.ThemeFragment
import com.pp.common.http.wanandroid.bean.home.BannerBean
import com.pp.home.databinding.FragmentHomeChildRealhomeBinding
import com.pp.ui.adapter.BannerAdapter
import com.pp.ui.utils.loadOriginal
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
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                mViewModel.bannerFlow.collectLatest {
                    dataList.clear()
                    dataList.addAll(it)
                    mBinding.carousel.jumpToIndex(mBinding.carousel.currentIndex)
                    bannerAdapter.start()
                }
            }
        }
    }

    override fun onFirstResume() {
        super.onFirstResume()
        mViewModel.getBanner2()
    }
}
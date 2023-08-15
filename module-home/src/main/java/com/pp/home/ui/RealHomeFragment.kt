package com.pp.home.ui

import android.view.View
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import com.pp.base.ThemeFragment
import com.pp.home.databinding.FragmentHomeChildRealhomeBinding
import com.pp.ui.adapter.BannerAdapter
import com.pp.ui.utils.load
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RealHomeFragment :
    ThemeFragment<FragmentHomeChildRealhomeBinding, RealHomeViewModel>() {
    override val mBinding: FragmentHomeChildRealhomeBinding by lazy {
        FragmentHomeChildRealhomeBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<RealHomeViewModel> {
        return RealHomeViewModel::class.java
    }


    override fun onFirstResume() {
        super.onFirstResume()
        lifecycleScope.launch(Dispatchers.IO) {
            mViewModel.getBanner().data?.also { dataList ->
                val bannerAdapter = object :
                    BannerAdapter(lifecycleScope, mBinding.motionlayout, mBinding.carousel) {
                    override fun count(): Int {
                        return dataList.size
                    }

                    override fun populate(view: View?, index: Int) {
//                        Log.e("TAG", "populate index: $index")
                        if (view is ImageView) {
                            view.load(dataList[index].imagePath.trim())
                        }
                    }

                    override fun onNewItem(index: Int) {
//                        Log.e("TAG", "onNewItem index: $index")
                    }
                }

                withContext(Dispatchers.Main) {
                    bannerAdapter.attachLifecycle(this@RealHomeFragment)
                    mBinding.carousel.run {
                        setAdapter(bannerAdapter)
                        jumpToIndex(0)
                    }
                }
            }
        }

    }
}
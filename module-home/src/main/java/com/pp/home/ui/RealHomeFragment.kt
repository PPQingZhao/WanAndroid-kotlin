package com.pp.home.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.constraintlayout.helper.widget.Carousel
import androidx.lifecycle.lifecycleScope
import com.pp.base.ThemeFragment
import com.pp.home.databinding.FragmentHomeChildRealhomeBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.observeOn

class RealHomeFragment :
    ThemeFragment<FragmentHomeChildRealhomeBinding, RealHomeViewModel>() {
    override val mBinding: FragmentHomeChildRealhomeBinding by lazy {
        FragmentHomeChildRealhomeBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<RealHomeViewModel> {
        return RealHomeViewModel::class.java
    }

    private fun start(): Job {
        job = lifecycleScope.launch {

            flow<Int> {
                while (true) {
                    delay(2000)
                    emit(0)
                    Log.e("TAG", "flow:  ${Thread.currentThread().name}")
                }
            }.flowOn(Dispatchers.IO)
                .collectLatest {
                    mBinding.carousel.run {
                        Log.e("TAG", "collect:  ${currentIndex}")
                        transitionToIndex(currentIndex + 1, 1000)
                    }
                }
        }
        return job!!
    }

    private var job: Job? = null
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        start()

        mBinding.motionlayout.setOnTouchListener { v, event ->

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    job?.cancel()
                }
                MotionEvent.ACTION_UP -> {
                    start()
                }
            }

            return@setOnTouchListener false
        }

        mBinding.carousel.setAdapter(object : Carousel.Adapter {
            override fun count(): Int {
                return 8
            }

            override fun populate(view: View?, index: Int) {
                Log.e("TAG", "populate index: $index")
            }

            override fun onNewItem(index: Int) {
            }

        })
    }
}
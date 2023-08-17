package com.pp.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.MotionEvent
import androidx.constraintlayout.helper.widget.Carousel
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class BannerAdapter(
    lifecycleScope: CoroutineScope,
    motionLayout: MotionLayout,
    carousel: Carousel,
) : Carousel.Adapter, DefaultLifecycleObserver {

    private var lifecycleScope: CoroutineScope? = lifecycleScope
    private var motionLayout: MotionLayout? = motionLayout
    private var carousel: Carousel? = carousel

    fun attachLifecycle(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    override fun onPause(owner: LifecycleOwner) {
        cancel()
    }

    override fun onResume(owner: LifecycleOwner) {
        start()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        cancel()
        motionLayout?.setOnTouchListener(null)
        lifecycleScope = null
        motionLayout = null
        carousel = null
    }

    private var job: Job? = null

    @SuppressLint("ClickableViewAccessibility")
    fun cancel() {
        job?.cancel()
        job = null
    }

    @SuppressLint("ClickableViewAccessibility")
    fun start() {
        cancel()
        if (count() <= 1) {
            return
        }
        motionLayout?.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    cancel()

                }
                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_CANCEL,
                -> {
                    motionLayout?.let {
                        if (it.progress == 0f) {
                            start()
                        } else {
                            it.addTransitionListener(object : TransitionAdapter() {
                                override fun onTransitionCompleted(
                                    motionLayout: MotionLayout?,
                                    currentId: Int,
                                ) {
                                    motionLayout?.removeTransitionListener(this)
                                    start()
                                }
                            })
                        }
                    }
                }
            }
            false
        }
        job = lifecycleScope?.launch {
            flow<Int> {
                while (true) {
                    delay(3000)
                    carousel?.run {
                        emit(currentIndex + 1)
                    }
//                    Log.e("TAG", "flow:  ${Thread.currentThread().name}")
                }
            }.flowOn(Dispatchers.IO)
                .collectLatest { targetIndex ->
                    carousel?.let {
//                        Log.e("TAG", "collect:  $targetIndex")
                        if (targetIndex == it.count) {
                            motionLayout?.addTransitionListener(object :
                                TransitionAdapter() {
                                override fun onTransitionCompleted(
                                    motionLayout: MotionLayout?,
                                    currentId: Int,
                                ) {
                                    it.transitionToIndex(0, 0)
                                    motionLayout?.removeTransitionListener(this)
                                }
                            })
                            it.transitionToIndex(targetIndex, 1000)
                        } else {
                            it.transitionToIndex(targetIndex, 1000)
                        }
                    }
                }
        }
    }
}
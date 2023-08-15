package com.pp.ui.utils

import android.animation.Animator
import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.AnimatorRes
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.vectordrawable.graphics.drawable.AnimatorInflaterCompat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.pp.ui.glide.GlideApp


/**
 * 动画
 */
@SuppressLint("RestrictedApi")
fun ImageView.starAnimator(lifecycle: Lifecycle, @AnimatorRes id: Int) {
    var animator = starAnimator(id)
    lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            animator?.cancel()
            animator = null
        }
    })
}

/**
 * 动画
 */
@SuppressLint("RestrictedApi")
fun ImageView.starAnimator(@AnimatorRes id: Int): Animator? {
    val loadAnimator = AnimatorInflaterCompat.loadAnimator(context, id)
    loadAnimator.setTarget(this)
    loadAnimator.start()
    return loadAnimator
}

/**
 * 加载原图尺寸
 */
fun ImageView.loadOriginal(path: String?, error: Drawable? = null) {

    val crossFadeFactory = DrawableCrossFadeFactory.Builder()
        .setCrossFadeEnabled(true)
        .build()

    GlideApp.with(context)
        .load(path)
        .dontAnimate()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .skipMemoryCache(false)
        .error(error)
        .transition(DrawableTransitionOptions.withCrossFade(crossFadeFactory))
        .into(object : DrawableImageViewTarget(this@loadOriginal) {
            override fun getSize(cb: SizeReadyCallback) {
                super.getSize { width, height ->
                    cb.onSizeReady(SIZE_ORIGINAL, SIZE_ORIGINAL)
                }
            }
        })

}

/**
 * 加载图片
 */
fun ImageView.load(path: String?, error: Drawable? = null) {

    val crossFadeFactory = DrawableCrossFadeFactory.Builder()
        .setCrossFadeEnabled(true)
        .build()

    GlideApp.with(context)
        .load(path)
        .dontAnimate()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .skipMemoryCache(false)
        .error(error)
        .transition(DrawableTransitionOptions.withCrossFade(crossFadeFactory))
        .into(this)
}

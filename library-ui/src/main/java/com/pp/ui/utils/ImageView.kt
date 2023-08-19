package com.pp.ui.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.pp.ui.glide.GlideApp


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
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
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
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .skipMemoryCache(false)
        .error(error)
        .transition(DrawableTransitionOptions.withCrossFade(crossFadeFactory))
        .into(this)
}

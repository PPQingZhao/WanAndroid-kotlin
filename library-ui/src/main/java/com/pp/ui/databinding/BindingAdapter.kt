package com.pp.ui.databinding

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.textfield.TextInputLayout
import com.pp.ui.adapter.BindingPagingDataAdapter
import com.pp.ui.utils.FloatingScrollerListener
import com.pp.ui.utils.attachRecyclerView
import com.pp.ui.utils.attachRefreshView
import com.pp.ui.utils.load

@androidx.databinding.BindingAdapter("android:background")
fun setBackground(iv: View, color: ColorStateList) {
//        Log.e("TAG", "res: $res")
    iv.background = ColorDrawable(color.defaultColor)
}

@SuppressLint("ResourceType")
@androidx.databinding.BindingAdapter("android:text")
fun setText(view: TextView, @StringRes resId: Int) {
//        Log.e("TAG", "res: $res")
    if (resId > 0) {
        view.setText(resId)
    } else {
        view.text = ""
    }
}

@androidx.databinding.BindingAdapter("enable")
fun setEnable(iv: View, enable: Boolean) {
//        Log.e("TAG", "res: $res")
    iv.isEnabled = enable
}

@androidx.databinding.BindingAdapter("errorMessage")
fun setErrorMessage(inputLayout: TextInputLayout, error: CharSequence) {
//        Log.e("TAG", "$error")
    inputLayout.error = error
}

@androidx.databinding.BindingAdapter("helperText")
fun setHelperText(inputLayout: TextInputLayout, text: CharSequence) {
//        Log.e("TAG", "$error")
    inputLayout.helperText = text
}

@androidx.databinding.BindingAdapter("android:tint")
fun setImageTint(iv: ImageView, color: ColorStateList?) {
    iv.imageTintList = color
}

@androidx.databinding.BindingAdapter(
    value = ["android:imageUrl", "android:errorDrawable"],
    requireAll = true
)
fun setImageUrl(iv: ImageView, url: String?, error: Drawable?) {
    iv.load(url, error)
}

@androidx.databinding.BindingAdapter("android:src")
fun setImageSrc(iv: ImageView, @DrawableRes src: Int) {
    iv.setImageResource(src)
}

@androidx.databinding.BindingAdapter("android:onFloatingClick")
fun onFloatingClick(floatButton: View, recyclerView: RecyclerView) {
    floatButton.setOnClickListener {
        recyclerView.scrollToPosition(0)
    }
}

@androidx.databinding.BindingAdapter("android:setupFloating")
fun setupFloating(floatButton: View, recyclerView: RecyclerView) {
    recyclerView.addOnScrollListener(FloatingScrollerListener(floatButton))
}

@androidx.databinding.BindingAdapter(
    value = ["android:pagingAdapter", "android:withLoadMore"],
    requireAll = true
)
fun setPagingAdapter(
    recyclerView: RecyclerView,
    pagingDataAdapter: BindingPagingDataAdapter<*>,
    withLoadMore: Boolean,
) {
    pagingDataAdapter.attachRecyclerView(recyclerView, withLoadMore = withLoadMore)
}

@androidx.databinding.BindingAdapter("android:attachPagingAdapter")
fun attachPagingAdapter(
    refreshLayout: SwipeRefreshLayout,
    pagingDataAdapter: BindingPagingDataAdapter<*>,
) {
    pagingDataAdapter.attachRefreshView(refreshLayout)
}

@androidx.databinding.BindingAdapter("android:animateVisibility")
fun animateVisibility(
    view: View,
    show: Boolean,
) {
    if (show) {
        view.visibility = View.VISIBLE
        val start = -view.height * 1f
        val end = 0f
        view.animate().translationY(start).translationY(end).setDuration(200).setListener(null)
            .start()
    } else {
        val start = 0f
        val end = -view.height * 1f
        view.animate().translationY(start).translationY(end).setDuration(200)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = View.GONE
                }
            }).start()

    }
}

@androidx.databinding.BindingAdapter("android:itemDecoration")
fun itemDecoration(
    recyclerView: RecyclerView,
    itemDecoration: RecyclerView.ItemDecoration,
) {
    recyclerView.addItemDecoration(itemDecoration)
}

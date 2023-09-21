package com.pp.ui.viewModel

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.Typeface
import android.widget.TextView
import androidx.databinding.ObservableField
import com.pp.theme.AppDynamicTheme

open class ItemCoinRankViewModel<Data : Any>(
    theme: AppDynamicTheme,
) : ItemDataViewModel<Data>(theme) {
    val rank = ObservableField<String>()
    val username = ObservableField<String>()
    val coinCount = ObservableField<String>()

}

@androidx.databinding.BindingAdapter("android:rankText")
fun rankText(
    textView: TextView,
    itemCoinRankViewModel: ItemCoinRankViewModel<*>,
) {
    val rank = itemCoinRankViewModel.rank.get()

    textView.text = rank
    setLinearGradient(textView, rank, itemCoinRankViewModel.theme.textColorSecondary.value!!)
}

@androidx.databinding.BindingAdapter("android:userNameText")
fun userNameText(
    textView: TextView,
    itemCoinRankViewModel: ItemCoinRankViewModel<*>,
) {
    val username = itemCoinRankViewModel.username.get()
    val rank = itemCoinRankViewModel.rank.get()
    textView.text = username
    setLinearGradient(textView, rank, itemCoinRankViewModel.theme.textColor.value!!)
}

fun setLinearGradient(
    textView: TextView,
    rank: String?,
    defaultColor: ColorStateList,
) {
    var startColor = Color.BLACK
    var endColor = Color.BLACK
    if (rank == "1") {
        textView.textSize = 18f
        textView.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD_ITALIC)
        startColor = Color.parseColor("#FFFF00")
        endColor = Color.parseColor("#EEC591")
    } else if (rank == "2"
        || rank == "3"
        || rank == "4"
        || rank == "5"
    ) {
        textView.textSize = 16f
        textView.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD_ITALIC)
        startColor = Color.parseColor("#EEC591")
        endColor = Color.parseColor("#8B7E66")
    } else {
        textView.textSize = 13f
        textView.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL)
        startColor = defaultColor.defaultColor
        endColor = defaultColor.defaultColor
    }

    val linearGradient = LinearGradient(
        0f,
        0f,
        0f,
        textView.paint.textSize,
        startColor,
        endColor,
        Shader.TileMode.CLAMP
    )
    textView.paint.shader = linearGradient
    textView.invalidate()
}
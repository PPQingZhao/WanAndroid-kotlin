package com.pp.ui.viewModel

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class ItemLocalBean(
    val itemType: Int,
    @StringRes val leftText: Int = 0,
    @StringRes val centerText: Int = 0,
    @StringRes val rightText: Int = 0,

    @DrawableRes val leftImage: Int = 0,
    @DrawableRes val centerImage: Int = 0,
    @DrawableRes val rightImage: Int = 0,
)
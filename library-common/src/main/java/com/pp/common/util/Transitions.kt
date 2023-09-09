package com.pp.common.util

import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.pp.common.constant.TRANSITION_DURATION

fun materialSharedAxis(axis: Int, forward: Boolean): MaterialSharedAxis {
    return MaterialSharedAxis(axis, forward).apply {
        duration = TRANSITION_DURATION
    }
}

fun materialElevationScale(growing: Boolean): MaterialElevationScale {
    return MaterialElevationScale(growing).apply {
        duration = TRANSITION_DURATION
    }
}

fun materialFadeThrough(): MaterialFadeThrough {
    return MaterialFadeThrough().apply {
        duration = TRANSITION_DURATION
    }
}


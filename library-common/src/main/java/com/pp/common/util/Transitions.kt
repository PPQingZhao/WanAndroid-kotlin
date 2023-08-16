package com.pp.common

import com.google.android.material.transition.MaterialSharedAxis
import com.pp.common.constant.Constants

fun materialSharedAxis(axis: Int, forward: Boolean): MaterialSharedAxis {
    return MaterialSharedAxis(axis, forward).apply {
        duration = Constants.TRANSITION_DURATION
    }
}

package com.pp.theme

import android.content.res.Resources.Theme

/**
 * 动态主题
 */
interface DynamicTheme {

    fun applyTheme(theme:Theme)
}
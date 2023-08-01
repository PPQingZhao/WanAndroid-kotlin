package com.pp.theme

import android.content.res.Resources.Theme

/**
 * 动态主题
 */
abstract class DynamicTheme {

    abstract fun applyTheme(theme: Theme)

}
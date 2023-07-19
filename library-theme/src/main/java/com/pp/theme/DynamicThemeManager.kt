package com.pp.theme

import android.content.res.Resources.Theme
import com.pp.theme.factory.Factory

open class DynamicThemeManager<DTheme : DynamicTheme> {

    private var mDynamicTheme: DTheme

    constructor(dynamicTheme: DTheme) {
        this.mDynamicTheme = dynamicTheme
    }

    fun apply(factory: Factory<Theme>): DTheme {
        return mDynamicTheme.apply {
            applyTheme(factory.create())
        }
    }

    fun apply(resTheme: Theme): DTheme {
        return mDynamicTheme.apply {
            applyTheme(resTheme)
        }
    }

    companion object {
        fun <DTheme : DynamicTheme> create(
            dynamicTheme: DTheme,
        ): DynamicThemeManager<DTheme> {
            return DynamicThemeManager(dynamicTheme)
        }
    }
}
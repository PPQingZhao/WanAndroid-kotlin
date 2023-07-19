package com.pp.theme.factory

import android.content.res.Resources
import android.content.res.Resources.Theme

class ResourcesThemeFactory(private val theme: Theme) : Factory<Theme> {

    override fun create(): Resources.Theme {
        return theme
    }
}
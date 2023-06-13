package com.pp.theme

import android.view.View

object ViewTreeAppThemeViewModel {
    operator fun set(view: View, theme: DynamicTheme?) {
        view.setTag(R.id.view_tree_dynamic_theme_view_model, theme)
    }

    operator fun get(view: View): DynamicTheme? {
        var found = view.getTag(R.id.view_tree_dynamic_theme_view_model)
        if (found != null) return found as DynamicTheme
        var parent = view.parent
        while (found == null && parent is View) {
            val parentView = parent as View
            found = parentView.getTag(R.id.view_tree_dynamic_theme_view_model)
            parent = parentView.parent
        }
        return if (found is DynamicTheme) found else null
    }
}
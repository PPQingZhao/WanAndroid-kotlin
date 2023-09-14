package com.pp.theme

import android.view.View

object ViewTreeAppThemeViewModel {
    operator fun set(view: View, theme: DynamicTheme?) {
        view.setTag(R.id.view_tree_dynamic_theme_view_model, theme)
    }

    inline fun <reified Theme : DynamicTheme> get(view: View): Theme? {
        var found = view.getTag(R.id.view_tree_dynamic_theme_view_model)
        if (found != null) {
            return if (found is Theme) found else null
        }
        var parent = view.parent
        while (found == null && parent is View) {
            val parentView = parent as View
            found = parentView.getTag(R.id.view_tree_dynamic_theme_view_model)
            parent = parentView.parent
        }
        return if (found is Theme) found else null
    }
}
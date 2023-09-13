package com.pp.common.util

import android.view.View
import com.pp.common.R
import com.pp.common.router.MultiRouterFragmentViewModel

object ViewTreeMultiRouterFragmentViewModel {
    fun set(view: View, theme: MultiRouterFragmentViewModel?) {
        view.setTag(R.id.view_tree_multi_router_fragment_view_model, theme)
    }

    inline fun <reified T : MultiRouterFragmentViewModel> get(view: View): T? {
        var found = view.getTag(R.id.view_tree_multi_router_fragment_view_model)
        if (found != null) {
            return if (found is T) found else null
        }
        var parent = view.parent
        while (found == null && parent is View) {
            val parentView = parent as View
            found = parentView.getTag(R.id.view_tree_multi_router_fragment_view_model)
            parent = parentView.parent
        }
        return if (found is T) found else null
    }
}
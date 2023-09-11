package com.pp.common.util

import android.view.View
import com.pp.common.R
import com.pp.common.router.MultiRouterFragmentViewModel

object ViewTreeMultiRouterFragmentViewModel {
    operator fun set(view: View, theme: MultiRouterFragmentViewModel?) {
        view.setTag(R.id.view_tree_multi_router_fragment_view_model, theme)
    }

    operator fun get(view: View): MultiRouterFragmentViewModel? {
        var found = view.getTag(R.id.view_tree_multi_router_fragment_view_model)
        if (found != null) return found as MultiRouterFragmentViewModel
        var parent = view.parent
        while (found == null && parent is View) {
            val parentView = parent as View
            found = parentView.getTag(R.id.view_tree_multi_router_fragment_view_model)
            parent = parentView.parent
        }
        return if (found is MultiRouterFragmentViewModel) found else null
    }
}
package com.pp.common.model

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.pp.base.browser.WebViewFragment
import com.pp.common.browser.CommonWebViewFragment
import com.pp.common.repository.BrowsingHistoryRepository
import com.pp.common.router.MultiRouterFragmentViewModel
import com.pp.ui.R
import com.pp.database.browsing.BrowsingHistory
import com.pp.router_service.RouterPath
import com.pp.theme.AppDynamicTheme
import com.pp.ui.viewModel.ItemHistoryViewModel

class ItemBrowsingHistoryViewModel(history: BrowsingHistory?, theme: AppDynamicTheme) :
    ItemHistoryViewModel<BrowsingHistory>(theme) {
    init {
        data = history
    }

    override fun onUpdateData(data: BrowsingHistory?) {
        title.set(data?.title ?: "")
        time.set(data?.latestTimeText ?: "")
    }

    override suspend fun onItemViewModelClick(view: View): Boolean {
        if (view.id == R.id.tv_delete) {
            onDelete()
        } else if (view.id == R.id.item_parent) {
            showWebFragment(view)
        } else {
            return false
        }
        return true
    }

    private fun showWebFragment(view: View) {
        val data = data ?: return

        val shareElement = view.findViewById<TextView>(R.id.tv_title)
        shareElement.transitionName = "transitionName${data.userId}"
        val bundle = Bundle().also {
            it.putString(WebViewFragment.WEB_VIEW_TITLE, title.get())
            it.putString(WebViewFragment.WEB_VIEW_URL, data.url)
            it.putBoolean(CommonWebViewFragment.WEB_COLLECT_BROWSING_HISTORY, false)
            it.putString(
                CommonWebViewFragment.WEB_VIEW_TRANSITION_NAME,
                shareElement.transitionName
            )
        }

        MultiRouterFragmentViewModel.showFragment(
            view,
            targetFragment = RouterPath.Web.fragment_web,
            tag = RouterPath.Web.fragment_web,
            arguments = bundle,
            sharedElement = shareElement
        )
    }

    private suspend fun onDelete() {
        data?.run {
            BrowsingHistoryRepository.remove(this)
        }
    }
}
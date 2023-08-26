package com.pp.navigation.model

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.pp.base.browser.WebViewFragment
import com.pp.common.app.App
import com.pp.common.browser.CommonWebViewFragment
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.util.ShareElementNavigation
import com.pp.router_service.RouterPath
import com.pp.theme.AppDynamicTheme
import com.pp.ui.viewModel.ItemTextViewModel

class ItemArticleTextViewModel(cidBean: ArticleBean?, theme: AppDynamicTheme) :
    ItemTextViewModel<ArticleBean>(theme) {

    init {
        updateData(cidBean)
    }

    override fun onUpdateData(data: ArticleBean?) {
        text.set(data?.title)
    }

    override fun onItemClick(view: View) {
        if (null == data) {
            return
        }

        val shareElement = view.findViewById<TextView>(com.pp.ui.R.id.tv_text)
        shareElement.transitionName = "transitionName${data!!.id}"
        val bundle = Bundle().also {
            it.putString(WebViewFragment.WEB_VIEW_TITLE, text.get())
            it.putString(WebViewFragment.WEB_VIEW_URL, data!!.link)
            it.putString(
                CommonWebViewFragment.WEB_VIEW_TRANSITION_NAME,
                shareElement.transitionName
            )
        }

        App.getInstance().navigation.value =
            RouterPath.Web.fragment_web to ShareElementNavigation(shareElement, bundle)
    }

}